package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.UpdateWalkingPlayerEvent;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.modules.player.BlockTweaks;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.InventoryUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.TextUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoTrap extends Module {

    private final Value<Integer> delay = addSetting(new Value("Delay/Place", 50, 0, 250));
    private final Value<Integer> blocksPerPlace = addSetting(new Value("Block/Place", 8, 1, 30));
    private final Value<Double> targetRange = addSetting(new Value("TargetRange", 10.0, 0.0, 20.0));
    private final Value<Double> range = addSetting(new Value("PlaceRange", 6.0, 0.0, 10.0));
    private final Value<TargetMode> targetMode = addSetting(new Value("Target", TargetMode.CLOSEST));
    private final Value<InventoryUtil.Switch> switchMode = addSetting(new Value("autoSwitch", InventoryUtil.Switch.NORMAL));
    private final Value<Boolean> rotate = addSetting(new Value("Rotate", true));
    private final Value<Boolean> raytrace = addSetting(new Value("Raytrace", false));
    private final Value<Pattern> pattern = addSetting(new Value("Pattern", Pattern.STATIC));
    private final Value<Integer> extend = addSetting(new Value("Extend", 4, 1, 4, v -> pattern.getValue() != Pattern.STATIC, "Extending the Trap."));
    private final Value<Boolean> antiScaffold = addSetting(new Value("AntiScaffold", false));
    private final Value<Boolean> antiStep = addSetting(new Value("AntiStep", false));
    private final Value<Boolean> legs = addSetting(new Value("Legs", false, v -> pattern.getValue() != Pattern.OPEN));
    private final Value<Boolean> platform = addSetting(new Value("Platform", false, v -> pattern.getValue() != Pattern.OPEN));
    private final Value<Boolean> antiDrop = addSetting(new Value("AntiDrop", false));
    private final Value<Double> speed = addSetting(new Value("Speed", 10.0, 0.0, 30.0));
    private final Value<Boolean> antiSelf = addSetting(new Value("AntiSelf", false));
    private final Value<Integer> eventMode = addSetting(new Value("Updates", 3, 1, 3));
    private final Value<Boolean> freecam = addSetting(new Value("Freecam", false));
    private final Value<Boolean> info = addSetting(new Value("Info", false));
    private final Value<Boolean> entityCheck = addSetting(new Value("NoBlock", true));
    private final Value<Boolean> disable = addSetting(new Value("TSelfMove", false));
    private final Value<Boolean> packet = addSetting(new Value("Packet", false));
    private final Value<Integer> retryer = addSetting(new Value("Retries", 4, 1, 15));

    private final Timer timer = new Timer();
    private boolean didPlace = false;
    private boolean switchedItem;
    public EntityPlayer target;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    public static boolean isPlacing = false;
    private boolean smartRotate = false;
    private final Map<BlockPos, Integer> retries = new HashMap<>();
    private final Timer retryTimer = new Timer();
    private BlockPos startPos = null;

    public AutoTrap() {
        super("AutoTrap", "Traps other players", Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if(fullNullCheck()) return;
        startPos = EntityUtil.getRoundedBlockPos(mc.player);
        lastHotbarSlot = mc.player.inventory.currentItem;
        retries.clear();
    }

    @Override
    public void onTick() {
        if(eventMode.getValue() == 3) {
            smartRotate = false;
            doTrap();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if(event.getStage() == 0 && eventMode.getValue() == 2) {
            smartRotate = rotate.getValue() && blocksPerPlace.getValue() == 1;
            doTrap();
        }
    }

    @Override
    public void onUpdate() {
        if(eventMode.getValue() == 1) {
            smartRotate = false;
            doTrap();
        }
    }

    @Override
    public String getDisplayInfo() {
        if(info.getValue() && target != null) {
            return target.getName();
        }
        return null;
    }

    @Override
    public void onDisable() {
        isPlacing = false;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        switchItem(true);
    }

    private void doTrap() {
        if(check()) {
            return;
        }

        switch(pattern.getValue()) {
            case STATIC:
                doStaticTrap();
                break;
            case SMART:
            case OPEN:
                doSmartTrap();
                break;
            default:
        }

        if(didPlace) {
            timer.reset();
        }
    }

    private void doSmartTrap() {
        List<Vec3d> placeTargets = EntityUtil.getUntrappedBlocksExtended(extend.getValue(), target, antiScaffold.getValue(), antiStep.getValue(), legs.getValue(), platform.getValue(), antiDrop.getValue(), raytrace.getValue());
        placeList(placeTargets);
    }

    private void doStaticTrap() {
        List<Vec3d> placeTargets = EntityUtil.targets(target.getPositionVector(), antiScaffold.getValue(), antiStep.getValue(), legs.getValue(), platform.getValue(), antiDrop.getValue(), raytrace.getValue());
        placeList(placeTargets);
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));

        for(Vec3d vec3d : list) {
            BlockPos position = new BlockPos(vec3d);
            int placeability = BlockUtil.isPositionPlaceable(position, raytrace.getValue());
            if(entityCheck.getValue() && placeability == 1 && ((switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && BlockTweaks.getINSTANCE().noBlock.getValue()))) && (retries.get(position) == null || retries.get(position) < retryer.getValue())) {
                placeBlock(position);
                retries.put(position, (retries.get(position) == null ? 1 : (retries.get(position) + 1)));
                retryTimer.reset();
                continue;
            }

            if(placeability == 3 && (!antiSelf.getValue() || !MathUtil.areVec3dsAligned(mc.player.getPositionVector(), vec3d))) {
                placeBlock(position);
            }
        }
    }

    private boolean check() {
        isPlacing = false;
        didPlace = false;
        placements = 0;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);

        if(this.isOff()) {
            return true;
        }

        if(disable.getValue() && (!startPos.equals(EntityUtil.getRoundedBlockPos(mc.player)))) {
            this.disable();
            return true;
        }

        if(retryTimer.passedMs(2000)) {
            retries.clear();
            retryTimer.reset();
        }

        if(obbySlot == -1) {
            if(switchMode.getValue() != InventoryUtil.Switch.NONE) {
                if(info.getValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> " + TextUtil.RED + "You are out of Obsidian.");
                }
                this.disable();
            }
            return true;
        }

        if(mc.player.inventory.currentItem != lastHotbarSlot && mc.player.inventory.currentItem != obbySlot) {
            lastHotbarSlot = mc.player.inventory.currentItem;
        }

        switchItem(true);
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        target = getTarget(targetRange.getValue(), targetMode.getValue() == TargetMode.UNTRAPPED);

        return target == null || (MoonHack.getModuleManager.isModuleEnabled("Freecam") && !freecam.getValue()) || !timer.passedMs(delay.getValue()) || switchMode.getValue() == InventoryUtil.Switch.NONE && mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(BlockObsidian.class);
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2) + 1;
        for(EntityPlayer player : mc.world.playerEntities) {
            if(EntityUtil.isntValid(player, range)) {
                continue;
            }

            if(pattern.getValue() == Pattern.STATIC && trapped && EntityUtil.isTrapped(player, antiScaffold.getValue(), antiStep.getValue(), legs.getValue(), platform.getValue(), antiDrop.getValue())) {
                continue;
            }

            if(pattern.getValue() != Pattern.STATIC && trapped && EntityUtil.isTrappedExtended(extend.getValue(), player, antiScaffold.getValue(), antiStep.getValue(), legs.getValue(), platform.getValue(), antiDrop.getValue(), raytrace.getValue())) {
                continue;
            }

            if(EntityUtil.getRoundedBlockPos(mc.player).equals(EntityUtil.getRoundedBlockPos(player)) && antiSelf.getValue()) {
                continue;
            }

            if(MoonHack.getSpeedManager.getPlayerSpeed(player) > speed.getValue()) {
                continue;
            }

            if(target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }

            if(mc.player.getDistanceSq(player) < distance) {
                target = player;
                distance = mc.player.getDistanceSq(player);
            }
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if(placements < blocksPerPlace.getValue() && mc.player.getDistanceSq(pos) <= MathUtil.square(range.getValue()) && switchItem(false)) {
            isPlacing = true;
            if(smartRotate) {
                isSneaking = BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, packet.getValue(), isSneaking);
            } else {
                isSneaking = BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), isSneaking);
            }
            didPlace = true;
            placements++;
        }
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, lastHotbarSlot, switchedItem, switchMode.getValue(), BlockObsidian.class);
        switchedItem = value[0];
        return value[1];
    }

    public enum Pattern {
        STATIC,
        SMART,
        OPEN
    }

    public enum TargetMode {
        CLOSEST,
        UNTRAPPED
    }
}
