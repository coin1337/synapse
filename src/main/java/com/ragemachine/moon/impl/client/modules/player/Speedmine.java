package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.api.event.events.BlockEvent;
import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.util.RenderUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Iterator;

import static com.ragemachine.moon.impl.util.BlockUtil.canBreak;
import static net.minecraft.network.play.client.CPacketPlayerDigging.Action.START_DESTROY_BLOCK;
import static net.minecraft.network.play.client.CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK;

public class Speedmine extends Module {

    public Value<Boolean> tweaks = addSetting(new Value("Tweaks", true));
    public Value<Mode> mode = addSetting(new Value("Mode", Mode.PACKET, v -> tweaks.getValue()));
    public Value<Float> damage = addSetting(new Value("Damage", 0.7f, 0.0f, 1.0f, v -> mode.getValue() == Mode.DAMAGE && tweaks.getValue()));
    public Value<Boolean> doubleBreak = addSetting(new Value("DoubleBreak", false));
    public Value<Boolean> render = addSetting(new Value("Render", false));
    public Value<Boolean> box = addSetting(new Value("Box", false, v -> render.getValue()));
    public Value<Boolean> outline = addSetting(new Value("Outline", true, v -> render.getValue()));
    private final Value<Integer> boxAlpha = addSetting(new Value("Box Alpha", 85, 0, 255, v -> box.getValue() && render.getValue()));
    private final Value<Float> lineWidth = addSetting(new Value("Line Width", 1.0f, 0.1f, 5.0f, v -> outline.getValue() && render.getValue()));

    private static Speedmine INSTANCE = new Speedmine();

    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private final Timer timer = new Timer();

    private boolean isMining = false;
    private BlockPos lastPos = null;
    private EnumFacing lastFacing = null;

    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Category.PLAYER, true, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Speedmine getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Speedmine();
        }
        return INSTANCE;
    }

    @Override
    public void onTick() {
        if(currentPos != null) {
            if(!mc.world.getBlockState(currentPos).equals(currentBlockState) || mc.world.getBlockState(currentPos).getBlock() == Blocks.AIR) {
                currentPos = null;
                currentBlockState = null;
            } else {
            }
        }
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }

        mc.playerController.blockHitDelay = 0;


        if (!Speedmine.mc.gameSettings.keyBindAttack.isKeyDown() && mode.getValue() == Mode.PACKET) {
            this.resetMining();
            return;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && mode.getValue() == Mode.PACKET) {
            Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
    }

    private void setMiningInfo(final BlockPos lastPos, final EnumFacing lastFacing) {
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void resetMining() {
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if(render.getValue() && currentPos != null) {
            Color color = new Color((timer.passedMs((int)(2000 * MoonHack.getServerManager.getTpsFactor())) ? 0 : 255), (timer.passedMs((int)(2000 * MoonHack.getServerManager.getTpsFactor())) ? 255 : 0), 0, 255);
            RenderUtil.drawBoxESP(currentPos, color, false, color, lineWidth.getValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), false);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }

        if (event.getStage() == 0) {

            if (mode.getValue() == Mode.PACKET && event.getPacket() instanceof CPacketPlayerDigging) {
                CPacketPlayerDigging packet = event.getPacket();
                if (packet != null && packet.getPosition() != null) {
                    try {
                        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(packet.getPosition()))) {
                            if (entity instanceof EntityEnderCrystal) {
                                showAnimation();
                                return;
                            }
                        }
                    } catch(Exception ignored) { }

                    if (packet.getAction().equals(START_DESTROY_BLOCK)) {
                        showAnimation(false, packet.getPosition(), packet.getFacing());
                    }

                    if (packet.getAction().equals(STOP_DESTROY_BLOCK)) {
                        showAnimation();
                    }
                }
            }
        }
        if (mode.getValue() == Mode.PACKET) {
            if (event.getPacket() instanceof CPacketPlayerDigging) {
                CPacketPlayerDigging cPacketPlayerDigging = (CPacketPlayerDigging) event.getPacket();
                final Iterator<Entity> iterator = Speedmine.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(cPacketPlayerDigging.getPosition())).iterator();
                Entity entity;
                while (iterator.hasNext()) {
                    entity = iterator.next();
                    if (entity instanceof EntityEnderCrystal) {
                        this.resetMining();
                        return;
                    } else if (entity instanceof EntityLivingBase) {
                        this.resetMining();
                        return;
                    } else {
                        continue;
                    }
                }
                if (cPacketPlayerDigging.getAction().equals((Object) CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.isMining = true;
                    this.setMiningInfo(cPacketPlayerDigging.getPosition(), cPacketPlayerDigging.getFacing());
                }
                if (cPacketPlayerDigging.getAction().equals((Object) CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.resetMining();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (fullNullCheck()) {
            return;
        }

        if (event.getStage() == 4 && tweaks.getValue()) {
            if (canBreak(event.pos)) {

                switch (this.mode.getValue()) {
                    case PACKET:
                        if(currentPos == null) {
                            currentPos = event.pos;
                            currentBlockState = mc.world.getBlockState(currentPos);
                            timer.reset();
                        }
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(START_DESTROY_BLOCK, event.pos, event.facing));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    case DAMAGE:
                        if (mc.playerController.curBlockDamageMP >= damage.getValue()) {
                            mc.playerController.curBlockDamageMP = 1.0f;
                        }
                        break;
                    case INSTANT:
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(START_DESTROY_BLOCK, event.pos, event.facing));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(STOP_DESTROY_BLOCK, event.pos, event.facing));
                        mc.playerController.onPlayerDestroyBlock(event.pos);
                        mc.world.setBlockToAir(event.pos);
                        break;
                }
            }

            if (doubleBreak.getValue()) {
                BlockPos above = event.pos.add(0, 1, 0);
                if (canBreak(above) && mc.player.getDistance(above.getX(), above.getY(), above.getZ()) <= 5f) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(START_DESTROY_BLOCK, above, event.facing));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(STOP_DESTROY_BLOCK, above, event.facing));
                    mc.playerController.onPlayerDestroyBlock(above);
                    mc.world.setBlockToAir(above);
                }
            }
        }
    }

    private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void showAnimation() {
        showAnimation(false, null, null);
    }

    @Override
    public String getDisplayInfo() {
        return mode.currentEnumName();
    }

    public enum Mode {
        PACKET,
        DAMAGE,
        INSTANT
    }
}
