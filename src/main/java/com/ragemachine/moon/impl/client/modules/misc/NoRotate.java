package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.TextUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//Wait on login and logoff
public class NoRotate extends Module {

    private Value<Integer> waitDelay = addSetting(new Value("Delay", 2500, 0, 10000));

    //Im not sure when the server sends you the packets. So to be sure it works I do the 2 booleans on logout and login...
    private Timer timer = new Timer();
    private boolean cancelPackets = true;
    private boolean timerReset = false;

    public NoRotate() {
        super("NoRotate", "broken", Category.MISC,true, false, false);
    }

    @Override
    public void onLogout() {
        cancelPackets = false;
    }

    @Override
    public void onLogin() {
        timer.reset();
        timerReset = true;
    }

    @Override
    public void onUpdate() {
        if(timerReset && !cancelPackets && timer.passedMs(waitDelay.getValue())) {
            Command.sendMessage("<NoRotate> " + TextUtil.RED + "This module might desync you!");
            cancelPackets = true;
            timerReset = false;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if(event.getStage() == 0 && cancelPackets) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook packet = event.getPacket();
                packet.yaw = mc.player.rotationYaw;
                packet.pitch = mc.player.rotationPitch;
            }
        }
    }
}
