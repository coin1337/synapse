package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.UpdateWalkingPlayerEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.modules.player.BlockTweaks;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.InventoryUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Surround extends Module {

    private final Value<Integer> delay = addSetting(new Value("Delay", 50, 0, 250));
    private final Value<Integer> blocksPerTick = addSetting(new Value("Place Ticks", 8, 1, 20));
    private final Value<Boolean> rotate = addSetting(new Value("Rotate", true));
    private final Value<Boolean> raytrace = addSetting(new Value("Raytrace", false));
    private final Value<InventoryUtil.Switch> switchMode = addSetting(new Value("autoSwitch", InventoryUtil.Switch.NORMAL));
    private final Value<Boolean> center = addSetting(new Value("Center", false));
    private final Value<Boolean> helpingBlocks = addSetting(new Value("Under Blocks", true));
    private final Value<Boolean> intelligent = addSetting(new Value("Strict", false, v -> helpingBlocks.getValue()));
    private final Value<Boolean> smart = addSetting(new Value("Smart", false));
    private final Value<MovementMode> movementMode = addSetting(new Value("Movement", MovementMode.STRICT));
    private final Value<Integer> eventMode = addSetting(new Value("Updates", 3, 1, 3, v -> false));
    private final Value<Boolean> floor = addSetting(new Value("Floor", false));
    private final Value<Boolean> noGhost = addSetting(new Value("Packet Place", false));

    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private final Set<Vec3d> extendingBlocks = new HashSet<>();
    private int extenders = 1;
    public static boolean isPlacing = false;
    private int obbySlot = -1;
    private boolean offHand = false;
    private final Map<BlockPos, Integer> retries = new HashMap<>();

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        lastHotbarSlot = mc.player.inventory.currentItem;
        startPos = EntityUtil.getRoundedBlockPos(mc.player);
        if (center.getValue() && !MoonHack.getModuleManager.isModuleEnabled("Freecam")) {
            MoonHack.getPositionManager.setPositionPacket(startPos.getX() + 0.5, startPos.getY(), startPos.getZ() + 0.5, true, true, true);
        }
        retries.clear();
        retryTimer.reset();
    }

    @Override
    public void onTick() {
        if(eventMode.getValue() == 3) {
            doFeetPlace();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if(event.getStage() == 0 && eventMode.getValue() == 2) {
            doFeetPlace();
        }
    }

    @Override
    public void onUpdate() {
        if(eventMode.getValue() == 1) {
            doFeetPlace();
        }
    }

    @Override
    public void onDisable() {
        if(nullCheck()) {
            return;
        }
        isPlacing = false;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        switchItem(true);
    }

    private void doFeetPlace() {
        if(check()) {
            return;
        }

        if(!EntityUtil.isSafe(mc.player, 0, floor.getValue())) {
            isSafe = 0;
            placeBlocks(mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(mc.player, 0, floor.getValue()), helpingBlocks.getValue(), false);
        } else if(!EntityUtil.isSafe(mc.player, -1, false)) {
            isSafe = 1;
            if(smart.getValue()) {
                placeBlocks(mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(mc.player, -1, false), false, false);
            }
        } else {
            isSafe = 2;
        }

        if(didPlace) {
            timer.reset();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for(Vec3d vec3d : vec3ds) {
            for(Vec3d pos : EntityUtil.getUnsafeBlockArray(mc.player, 0, floor.getValue())) {
                if(vec3d.equals(pos)) {
                    matches++;
                }
            }
        }
        if(matches == 2) {
            return mc.player.getPositionVector().add(vec3ds[0].add(vec3ds [1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping) {
        int helpings = 0;
        boolean gotHelp = true;
        for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            helpings++;
            if(isHelping && !intelligent.getValue() && helpings > 1) {
                return false;
            }
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, raytrace.getValue())) {
                case -1:
                    continue;
                case 1:
                    if((switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && BlockTweaks.getINSTANCE().noBlock.getValue())) && (retries.get(position) == null || retries.get(position) < 4)) {
                        placeBlock(position);
                        retries.put(position, (retries.get(position) == null ? 1 : (retries.get(position) + 1)));
                        retryTimer.reset();
                        continue;
                    }
                    continue;
                case 2:
                    if (hasHelpingBlocks) {
                        gotHelp = placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true);
                    } else {
                        continue;
                    }
                case 3:
                    if(gotHelp) {
                        placeBlock(position);
                    }
                    if(isHelping) {
                        return true;
                    }
            }
        }
        return false;
    }

    private boolean check() {
        if(nullCheck()) {
            return true;
        }

        offHand = InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        isPlacing = false;
        didPlace = false;
        extenders = 1;
        placements = 0;
        obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);

        if(this.isOff()) {
            return true;
        }

        if(retryTimer.passedMs(2500)) {
            retries.clear();
            retryTimer.reset();
        }

        switchItem(true);


        isSneaking = EntityUtil.stopSneaking(isSneaking);

        if(mc.player.inventory.currentItem != lastHotbarSlot && mc.player.inventory.currentItem != obbySlot && mc.player.inventory.currentItem != echestSlot) {
            lastHotbarSlot = mc.player.inventory.currentItem;
        }

        switch(movementMode.getValue()) {
            case NONE:
                break;
            case OLD:
                if(!startPos.equals(EntityUtil.getRoundedBlockPos(mc.player))) {
                }
            case STRICT:
                if(MoonHack.getSpeedManager.getSpeedKpH() > 10) {
                    return true;
                }
                break;
            case OFF:
                if(MoonHack.getSpeedManager.getSpeedKpH() > 10) {
                                   }
        }
        return MoonHack.getModuleManager.isModuleEnabled("Freecam") || !timer.passedMs(delay.getValue()) || switchMode.getValue() == InventoryUtil.Switch.NONE && mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(BlockObsidian.class);
    }

    private void placeBlock(BlockPos pos) {
        if(placements < blocksPerTick.getValue() && switchItem(false)) {
            isPlacing = true;
            isSneaking = BlockUtil.placeBlock(pos, offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), isSneaking);
            didPlace = true;
            placements++;
        }
    }

    private boolean switchItem(boolean back) {
        if(offHand) {
            return true;
        }
        boolean[] value = InventoryUtil.switchItem(back, lastHotbarSlot, switchedItem, switchMode.getValue(), obbySlot == -1 ? BlockEnderChest.class : BlockObsidian.class);
        switchedItem = value[0];
        return value[1];
    }

    public enum MovementMode {
        NONE,
        OLD,
        STRICT,
        OFF
    }
}
