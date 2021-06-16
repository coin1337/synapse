package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.UpdateWalkingPlayerEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.modules.player.BlockTweaks;
import com.ragemachine.moon.impl.client.modules.player.Freecam;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.InventoryUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: THIS
public class Selftrap extends Module {

    public Value<Mode> mode = addSetting(new Value("Mode", Mode.OBSIDIAN));
    public Value<Bind> obbyBind = addSetting(new Value("Obsidian", new Bind(-1)));
    private final Value<Integer> delay = addSetting(new Value("Place Delay", 50, 0, 250));
    private final Value<Integer> blocksPerTick = addSetting(new Value("Block Ticking", 8, 1, 20));
    private final Value<Boolean> rotate = addSetting(new Value("Rotate", true));
    private final Value<Boolean> disable = addSetting(new Value("Disable", true));
    private final Value<Integer> disableTime = addSetting(new Value("Ms/Disable", 200, 1, 250));
    private final Value<InventoryUtil.Switch> switchMode = addSetting(new Value("autoSwitch", InventoryUtil.Switch.NORMAL));
    private final Value<Boolean> onlySafe = addSetting(new Value("Strict", true));
    private final Value<Boolean> packet = addSetting(new Value("Packet", false));

    public Mode currentMode = Mode.OBSIDIAN;
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private boolean accessedViaBind = false;
    private int blocksThisTick = 0;
    private final Map<BlockPos, Integer> retries = new HashMap<>();
    private final Timer retryTimer = new Timer();
    private boolean isSneaking;
    private boolean hasOffhand = false;
    private boolean placeHighWeb = false;
    private int lastHotbarSlot = -1;
    private boolean switchedItem = false;

    public Selftrap() {
        super("Selftrap", "Lure your enemies in!", Category.COMBAT, true, false, true);
    }

    @Override
    public void onEnable() {
        lastHotbarSlot = mc.player.inventory.currentItem;

        if(!accessedViaBind) {
            currentMode = mode.getValue();
        }
    }

    @Override
    public void onTick() {
        if(this.isOn() && !(blocksPerTick.getValue() == 1 && rotate.getValue())) {
            doHoleFill();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if(this.isOn() && event.getStage() == 0 && blocksPerTick.getValue() == 1 && rotate.getValue()) {
            doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        switchItem(true);
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        retries.clear();
        accessedViaBind = false;
        hasOffhand = false;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            if (obbyBind.getValue().getKey() == Keyboard.getEventKey()) {
                accessedViaBind = true;
                currentMode = Mode.OBSIDIAN;
                this.toggle();
            }
        }
    }

    private void doHoleFill() {
        if(check()) {
            return;
        }

        if(placeHighWeb) {
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
            placeBlock(pos);
            placeHighWeb = false;
        }

        for(BlockPos position : getPositions()) {
            int placeability = BlockUtil.isPositionPlaceable(position, false);
            if(placeability == 1) {
                switch(currentMode) {
                    case WEBS:
                        placeBlock(position);
                        break;
                    case OBSIDIAN:
                        if((switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && BlockTweaks.getINSTANCE().noBlock.getValue()))) {
                            if(retries.get(position) == null || retries.get(position) < 4) {
                                placeBlock(position);
                                retries.put(position, (retries.get(position) == null ? 1 : (retries.get(position) + 1)));
                            }
                        }
                        break;
                }
            }

            if(placeability == 3) {
                placeBlock(position);
            }
        }
    }

    private List<BlockPos> getPositions() {
        List<BlockPos> positions = new ArrayList<>();
        switch(currentMode) {
            case WEBS:
                positions.add(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
                break;
            case OBSIDIAN:
                positions.add(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ));
                int placeability = BlockUtil.isPositionPlaceable(positions.get(0), false);
                switch(placeability) {
                    case 0:
                        return new ArrayList<>();
                    case 3:
                        return positions;
                    case 1:
                        if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
                            return positions;
                        }
                    case 2:
                        positions.add(new BlockPos(mc.player.posX + 1, mc.player.posY + 1, mc.player.posZ));
                        positions.add(new BlockPos(mc.player.posX + 1, mc.player.posY + 2, mc.player.posZ));
                        break;
                    default:
                }
        }
        positions.sort(Comparator.comparingDouble(BlockPos::getY));
        return positions;
    }

    private void placeBlock(BlockPos pos) {
        if(blocksThisTick < blocksPerTick.getValue()) {
            if(switchItem(false)) {
                boolean smartRotate = blocksPerTick.getValue() == 1 && rotate.getValue();
                if (smartRotate) {
                    isSneaking = BlockUtil.placeBlockSmartRotate(pos, hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, packet.getValue(), isSneaking);
                } else {
                    isSneaking = BlockUtil.placeBlock(pos, hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), isSneaking);
                }
                timer.reset();
                //placedThisTick = true;
                blocksThisTick++;
            }
        }
    }

    private boolean check() {
        if(fullNullCheck() || (disable.getValue() && offTimer.passedMs(disableTime.getValue()))) {
            this.disable();
            return true;
        }

        if(mc.player.inventory.currentItem != lastHotbarSlot && mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(currentMode == Mode.WEBS ? BlockWeb.class : BlockObsidian.class)) {
            lastHotbarSlot = mc.player.inventory.currentItem;
        }

        switchItem(true);

        if(MoonHack.getModuleManager.isModuleEnabled(Freecam.class)) {
            return true;
        }

        //placedThisTick = false;
        blocksThisTick = 0;
        isSneaking = EntityUtil.stopSneaking(isSneaking);

        if(retryTimer.passedMs(2000)) {
            retries.clear();
            retryTimer.reset();
        }

        int targetSlot = -1;
        switch (currentMode) {
            case WEBS:
                hasOffhand = InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockWeb.class);
                targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
                break;
            case OBSIDIAN:
                hasOffhand = InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
                targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                break;
            default:
        }

        if(onlySafe.getValue() && !EntityUtil.isSafe(mc.player)) {
            this.disable();
            return true;
        }


        return !timer.passedMs(delay.getValue());
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, lastHotbarSlot, switchedItem, switchMode.getValue(), currentMode == Mode.WEBS ? BlockWeb.class : BlockObsidian.class);
        switchedItem = value[0];
        return value[1];
    }

    public enum Mode {
        WEBS,
        OBSIDIAN
    }
}
