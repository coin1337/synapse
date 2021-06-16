
package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.util.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.api.event.events.UpdateWalkingPlayerEvent;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.client.modules.Module;

import java.awt.*;

public class Scaffold extends Module
{
    public Value<Boolean> rotation;
    private final Timer timer;
    BlockPos renderPos;

    public Scaffold() {
        super("Scaffold", "Places Blocks underneath you.", Category.PLAYER, true, false, false);
        this.rotation = (Value<Boolean>)addSetting(new Value("Rotate", false));
        this.timer = new Timer();
    }

    private final Value<Boolean> render = addSetting(new Value("Render",  true));
    private final Value<Boolean> outline = addSetting(new Value("Render",  true));
    public Value<Boolean> box = addSetting(new Value("Box",true));
    public Value<Integer> red = addSetting(new Value("Red", 255, 0, 255));
    public Value<Integer> green = addSetting(new Value("Green", 255, 0, 255));
    public Value<Integer> blue = addSetting(new Value("Blue", 255, 0, 255));
    public final Value<Float> alpha = addSetting(new Value<Float>("Alpha", 255.0f, 0.1f, 255.0f));
    private final Value<Integer> boxAlpha = addSetting(new Value("BoxAlpha", 125, 0, 255));
    private final Value<Float> lineWidth = addSetting(new Value("LineWidth", 1.5F, 0.1F, 5.0F));



    @Override
    public void onEnable() {
        final EntityPlayer player = null;
        this.timer.reset();
        if (player != null) {
            renderPos = EntityUtil.getPlayerPosMinus1Y(player);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(final UpdateWalkingPlayerEvent event) {

        if (this.isOff() || Hack.fullNullCheck() || event.getStage() == 0) {
            return;
        }
        if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.timer.reset();
        }
        final BlockPos playerBlock = EntityUtil.getPlayerPosWithEntity();
        if (BlockUtil.isScaffoldPos(playerBlock.add(0, -1, 0))) {
            if (BlockUtil.isValidBlock(playerBlock.add(0, -2, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, -1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
                }
                this.place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
            }
        }
    }

    public void onRender3D(Render3DEvent event) {
        if (renderPos != null && render.getValue()) {
            RenderUtil.drawBoxESP(renderPos, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), outline.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), lineWidth.getValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true);
        }

    }

    public void place(final BlockPos posI, final EnumFacing face) {
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        }
        else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        }
        else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        }
        else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        }
        else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        final int oldSlot = Scaffold.mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Scaffold.mc.player.inventory.getStackInSlot(i);
            if (!InventoryUtil.isNull(stack) && stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
                newSlot = i;
                break;
            }
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        if (!Scaffold.mc.player.isSneaking()) {
            final Block block = Scaffold.mc.world.getBlockState(pos).getBlock();
            if (BlockUtil.blackList.contains(block)) {
                Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction((Entity)Scaffold.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                crouched = true;
            }
        }
        if (!(Scaffold.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            Scaffold.mc.player.connection.sendPacket(new CPacketHeldItemChange(newSlot));
            Scaffold.mc.player.inventory.currentItem = newSlot;
            Scaffold.mc.playerController.updateController();
        }
        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = Scaffold.mc.player;
            player.motionX *= 0.3;
            final EntityPlayerSP player2 = Scaffold.mc.player;
            player2.motionZ *= 0.3;
            Scaffold.mc.player.jump();
            if (this.timer.passedMs(1500L)) {
                Scaffold.mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.getValue()) {
            final float[] angle = MathUtil.calcAngle(Scaffold.mc.player.getPositionEyes(Scaffold.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            Scaffold.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], (float)MathHelper.normalizeAngle((int)angle[1], 360), Scaffold.mc.player.onGround));
        }
        Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
        Scaffold.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        Scaffold.mc.player.inventory.currentItem = oldSlot;
        Scaffold.mc.playerController.updateController();
        if (crouched) {
            Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction((Entity)Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
