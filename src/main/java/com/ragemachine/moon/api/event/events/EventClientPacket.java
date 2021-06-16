package com.ragemachine.moon.api.event.events;

import net.minecraft.network.Packet;

public class EventClientPacket extends EventPacket2
{
    public EventClientPacket(Packet<?> packet, Stage stage)
    {
        super(packet, stage);
    }
}