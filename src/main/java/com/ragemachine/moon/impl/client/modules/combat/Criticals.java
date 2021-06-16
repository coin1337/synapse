package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Criticals extends Module {

    private Value<Mode> mode = addSetting(new Value("Mode", Mode.PACKET));
    public Value<Boolean> noDesync = addSetting(new Value("NoDesync", true));
    private Value<Integer> packets = addSetting(new Value("Packets", 2, 1, 4, v -> mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    private Value<Integer> desyncDelay = addSetting(new Value("DesyncDelay", 10, 0, 500, v -> mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    public Value<Boolean> cancelFirst = addSetting(new Value("CancelFirst32k", true));
    public Value<Integer> delay32k = addSetting(new Value("32kDelay", 25, 0, 500, v -> cancelFirst.getValue()));

    private Timer timer = new Timer();
    private Timer timer32k = new Timer();
    private boolean firstCanceled = false;
    private boolean resetTimer = false;

    public Criticals() {
        super("Criticals", "Scores criticals for you", Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {

        if(event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                
                if(firstCanceled) {
                    timer32k.reset();
                    resetTimer = true;
                    timer.setMs(desyncDelay.getValue() + 1);
                    firstCanceled = false;
                    return;
                }

                if(resetTimer && !timer32k.passedMs(delay32k.getValue())) {
                    return;
                } else if(resetTimer && timer32k.passedMs(delay32k.getValue())) {
                    resetTimer = false;
                }

                if(!timer.passedMs(desyncDelay.getValue())) {
                    return;
                }

                if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && (packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase || !noDesync.getValue()) && !(mc.player.isInWater() || mc.player.isInLava())) {
                    if (mode.getValue() == Mode.PACKET) {
                        switch(packets.getValue()) {
                            case 1:
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                break;
                            case 2:
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1E-5, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                break;
                            case 3:
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0125D, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                break;
                            case 4:
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1625, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 4.0E-6, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0E-6, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                                mc.player.connection.sendPacket(new CPacketPlayer());
                                mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld(mc.world)));
                                break;
                            default:
                        }
                    } else {
                        mc.player.jump();
                    }
                    timer.reset();
                }
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return mode.currentEnumName();
    }

    public enum Mode {
        JUMP,
        PACKET
    }
}
