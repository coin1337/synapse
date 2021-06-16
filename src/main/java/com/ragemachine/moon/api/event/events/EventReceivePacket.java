

package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.MoonEvent;
import net.minecraft.network.Packet;

public class EventReceivePacket extends MoonEvent {
    private Packet packet;
    
    public EventReceivePacket(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}
