package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.InventoryUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Crasher
        extends Module {
    private final Value<Mode> mode = this.addSetting(new Value<Mode>("Mode", Mode.ONCE));
    private final Value<Boolean> oneDot15 = this.addSetting(new Value<Boolean>("1.15", false));
    private final Value<Float> placeRange = this.addSetting(new Value<Float>("PlaceRange", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f)));
    private final Value<Integer> crystals = this.addSetting(new Value<Integer>("Positions", 100, 0, 1000));
    private final Value<Integer> coolDown = this.addSetting(new Value<Integer>("CoolDown", 400, 0, 1000));
    private final Value<InventoryUtil.Switch> switchMode = this.addSetting(new Value<InventoryUtil.Switch>("autoSwitch", InventoryUtil.Switch.NORMAL));
    public Value<Integer> sort = this.addSetting(new Value<Integer>("Sort", 0, 0, 2));
    private boolean offhand = false;
    private boolean mainhand = false;
    private Timer timer = new Timer();
    private int lastHotbarSlot = -1;
    private boolean switchedItem = false;
    private boolean chinese = false;

    public Crasher() {
        super("CrystalCrash", "Attempts to crash chinese AutoCrystals", Module.Category.COMBAT, false, false, true);
    }


    public void onEnable() {
        this.chinese = false;
        if (!fullNullCheck() && this.timer.passedMs((long)(Integer)this.coolDown.getValue())) {
            this.lastHotbarSlot = mc.player.inventory.currentItem;
            if (this.mode.getValue() == Crasher.Mode.ONCE) {
                this.placeCrystals();
                this.disable();
            }

        } else {
            this.disable();
        }
    }

    public void onDisable() {
        this.timer.reset();
        if (this.mode.getValue() == Crasher.Mode.SPAM) {
            this.switchItem(true);
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!fullNullCheck() && event.phase != TickEvent.Phase.START && (!this.isOff() || !this.timer.passedMs(10L) && this.mode.getValue() != Crasher.Mode.SPAM)) {
            if (this.mode.getValue() == Crasher.Mode.SPAM) {
                this.placeCrystals();
            } else {
                this.switchItem(true);
            }

                    }
    }

    private void placeCrystals() {
        this.offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        this.mainhand = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL;
        int crystalcount = 0;
        List blocks = BlockUtil.possiblePlacePositions((Float)this.placeRange.getValue(), false, (Boolean)this.oneDot15.getValue());
        if ((Integer)this.sort.getValue() == 1) {
            blocks.sort(Comparator.comparingDouble((hole) -> {
                return mc.player.getDistanceSq((Entity) hole);
            }));
        } else if ((Integer)this.sort.getValue() == 2) {
            blocks.sort(Comparator.comparingDouble((hole) -> {
                return -mc.player.getDistanceSq((Entity) hole);
            }));
        }

        for(Iterator var3 = blocks.iterator(); var3.hasNext(); ++crystalcount) {
            BlockPos pos = (BlockPos)var3.next();
            if (this.isOff() || crystalcount >= (Integer)this.crystals.getValue()) {
                break;
            }

            this.placeCrystal(pos);
        }

    }

    private void placeCrystal(BlockPos pos) {
        if (!this.chinese && !this.mainhand && !this.offhand && !this.switchItem(false)) {
            this.disable();
        } else {
            RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY() - 0.5D, (double)pos.getZ() + 0.5D));
            EnumFacing facing = result != null && result.sideHit != null ? result.sideHit : EnumFacing.UP;
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    private boolean switchItem(boolean back) {
        this.chinese = true;
        if (this.offhand) {
            return true;
        } else {
            boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), Items.END_CRYSTAL);
            this.switchedItem = value[0];
            return value[1];
        }
    }

    public static enum Mode {
        ONCE,
        SPAM;
    }
}

