
package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.impl.client.Hack;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.List;

public class PacketManager
        extends Hack {
    private final List<Packet<?>> noEventPackets = new ArrayList();

    public void sendPacketNoEvent(Packet<?> packet) {
        if (packet == null) return;
        if (PacketManager.nullCheck()) return;
        this.noEventPackets.add(packet);
        PacketManager.mc.player.connection.sendPacket(packet);
    }

    public boolean shouldSendPacket(Packet<?> packet) {
        if (!this.noEventPackets.contains(packet)) return true;
        this.noEventPackets.remove(packet);
        return false;
    }
}

