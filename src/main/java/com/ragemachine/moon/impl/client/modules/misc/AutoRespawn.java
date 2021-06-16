package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawn extends Module {

    public Value<Boolean> antiDeathScreen = addSetting(new Value("AntiDeathScreen", true));
    public Value<Boolean> deathCoords = addSetting(new Value("DeathCoords", false));
    public Value<Boolean> respawn = addSetting(new Value("Respawn", true));

    public AutoRespawn() {
        super("AutoRespawn", "Respawns you when you die.", Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if(deathCoords.getValue()) {
                if(event.getGui() instanceof GuiGameOver) {
                    Command.sendMessage(String.format("You died at x %d y %d z %d", (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ));
                }
            }

            if((respawn.getValue() && mc.player.getHealth() <= 0) || (antiDeathScreen.getValue() && mc.player.getHealth() > 0)) {
                event.setCanceled(true);
                mc.player.respawnPlayer();
            }
        }
    }
}
