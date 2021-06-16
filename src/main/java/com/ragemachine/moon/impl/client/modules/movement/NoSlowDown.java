package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown extends Module {

    //Some nigga sent this to me i cant find who, it dont work any ways xD

    public Value<Boolean> guiMove = addSetting(new Value("GuiMove", true));
    public Value<Boolean> soulSand = addSetting(new Value("SoulSand", true));

    private static NoSlowDown INSTANCE = new NoSlowDown();
    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from getting slowed down.", Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
    }

    @Override
    public void onUpdate() {
        if (guiMove.getValue()) {
            if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
                EntityPlayerSP player;
                if (Keyboard.isKeyDown(200)) {
                    player = mc.player;
                    player.rotationPitch -= 5.0F;
                }

                if (Keyboard.isKeyDown(208)) {
                    player = mc.player;
                    player.rotationPitch += 5.0F;
                }

                if (Keyboard.isKeyDown(205)) {
                    player = mc.player;
                    player.rotationYaw += 5.0F;
                }

                if (Keyboard.isKeyDown(203)) {
                    player = mc.player;
                    player.rotationYaw -= 5.0F;
                }

                if (mc.player.rotationPitch > 90.0F) {
                    mc.player.rotationPitch = 90.0F;
                }

                if (mc.player.rotationPitch < -90.0F) {
                    mc.player.rotationPitch = -90.0F;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    // Check MixinBlockSoulSand for soulsand slowdown nullification
}
