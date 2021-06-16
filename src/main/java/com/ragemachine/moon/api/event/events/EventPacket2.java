package com.ragemachine.moon.api.event.events;

import net.minecraft.network.Packet;

public class EventPacket2 extends MinecraftEvent
{
    private Packet<?>  _packet;
    
    public EventPacket2(Packet<?>  packet, Stage stage)
    {
        super(stage);
        _packet = packet;
    }

    public Packet<?> getPacket()
    {
        return _packet;
    }
}