package com.ragemachine.moon.api.event.events;


import com.ragemachine.moon.api.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EventMotionUpdate extends EventStage {

    public int stage;

    public EventMotionUpdate(int stage) {
        super();
        this.stage = stage;
    }
    
}