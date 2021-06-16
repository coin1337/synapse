
package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class DeathEvent
extends EventStage {
    public EntityPlayer player;

    public DeathEvent(EntityPlayer player) {
        this.player = player;
    }
}

