package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING;

public class AntiHunger extends Module {

    public Value<Boolean> cancelSprint = addSetting(new Value("CancelSprint", true));

    public AntiHunger() {
        super("AntiHunger", "Prevents you from getting Hungry", Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            packet.onGround = mc.player.fallDistance >= 0 || mc.playerController.isHittingBlock;
        }

        if(cancelSprint.getValue() && event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet = event.getPacket();
            if (packet.getAction() == START_SPRINTING || packet.getAction() == STOP_SPRINTING) {
                event.setCanceled(true);
            }
        }
    }
}
