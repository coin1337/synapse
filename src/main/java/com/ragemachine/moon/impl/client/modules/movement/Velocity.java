package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.api.event.events.PushEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

    public Value<Boolean> noPush = addSetting(new Value("NoPush", true));
    public Value<Float> horizontal = addSetting(new Value("Horizontal", 0.0f, 0.0f, 100.0f));
    public Value<Float> vertical = addSetting(new Value("Vertical", 0.0f, 0.0f, 100.0f));
    public Value<Boolean> explosions = addSetting(new Value("Explosions", true));
    public Value<Boolean> bobbers = addSetting(new Value("Bobbers", true));
    public Value<Boolean> water = addSetting(new Value("Water", false));
    public Value<Boolean> blocks = addSetting(new Value("Blocks", false));
    public Value<Boolean> ice = addSetting(new Value("Ice", false));

    private static Velocity INSTANCE = new Velocity();

    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Category.MOVEMENT, true, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Velocity getINSTANCE() {
        if(INSTANCE == null) {
            INSTANCE = new Velocity();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onDisable() {
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if(event.getStage() == 0 && mc.player != null) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                SPacketEntityVelocity velocity = event.getPacket();
                if (velocity.getEntityID() == mc.player.entityId) {
                    if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                        event.setCanceled(true);
                        return;
                    }

                    velocity.motionX *= horizontal.getValue();
                    velocity.motionY *= vertical.getValue();
                    velocity.motionZ *= horizontal.getValue();
                }
            }

            if (event.getPacket() instanceof SPacketEntityStatus && bobbers.getValue()) {
                SPacketEntityStatus packet = event.getPacket();
                if (packet.getOpCode() == 31) {
                    Entity entity = packet.getEntity(mc.world);
                    if (entity instanceof EntityFishHook) {
                        EntityFishHook fishHook = (EntityFishHook)entity;
                        if (fishHook.caughtEntity == mc.player) {
                            event.setCanceled(true);
                        }
                    }
                }
            }

            if (explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
                if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                    event.setCanceled(true);
                    return;
                }

                SPacketExplosion velocity = event.getPacket();
                velocity.motionX *= horizontal.getValue();
                velocity.motionY *= vertical.getValue();
                velocity.motionZ *= horizontal.getValue();
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if(event.getStage() == 0 && noPush.getValue() && event.entity.equals(mc.player)) {
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                event.setCanceled(true);
                return;
            }

            event.x = -event.x * horizontal.getValue();
            event.y = -event.y * vertical.getValue();
            event.z = -event.z * horizontal.getValue();
        } else if(event.getStage() == 1 && blocks.getValue()) {
            event.setCanceled(true);
        } else if(event.getStage() == 2 && water.getValue() && mc.player != null && mc.player.equals(event.entity)) {
            event.setCanceled(true);
        }
    }
}
