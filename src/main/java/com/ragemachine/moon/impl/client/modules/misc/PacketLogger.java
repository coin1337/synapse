package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketLogger extends Module {

    public Value<Packets> packets = addSetting(new Value("Packets", Packets.OUTGOING));
    public Value<Boolean> chat = addSetting(new Value("Chat", false));

    public PacketLogger() {
        super("PacketLogger", "Logs stuff", Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if(packets.getValue() == Packets.OUTGOING || packets.getValue() == Packets.ALL) {
            if(chat.getValue()) {
                Command.sendMessage(event.getPacket().toString());
            } else {
                System.out.println(event.getPacket().toString());
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if(packets.getValue() == Packets.INCOMING || packets.getValue() == Packets.ALL) {
            boolean xD = true;
            if(mc.player != null) {
                xD = mc.player.inventoryContainer.getSlot(6).getStack().getItem() == Items.ELYTRA;
                System.out.println(xD);
            }
            if(chat.getValue()) {
                Command.sendMessage(xD + event.getPacket().toString());
            } else {
                System.out.println(xD + event.getPacket().toString());
            }
        }
    }

    public enum Packets {
        NONE,
        INCOMING,
        OUTGOING,
        ALL
    }

}
