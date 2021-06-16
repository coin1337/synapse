package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class AutoLog extends Module {

    private Value<Float> health = addSetting(new Value("Health", 16.0f, 0.1f, 36.0f));
    private Value<Boolean> logout = addSetting(new Value("LogoutOff", true));

    private static AutoLog INSTANCE = new AutoLog();

    public AutoLog() {
        super("AutoLog", "Logs when in danger.", Category.MISC, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static AutoLog getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AutoLog();
        }
        return INSTANCE;
    }

    @Override
    public void onTick() {
        if(!nullCheck()) {
            if (mc.player.getHealth() <= health.getValue()) {
                MoonHack.getModuleManager.disableModule("AutoReconnect");
                mc.player.connection.sendPacket(new SPacketDisconnect(new TextComponentString("AutoLogged")));
                if(logout.getValue()) {
                    this.disable();
                }
            }
        }
    }
}
