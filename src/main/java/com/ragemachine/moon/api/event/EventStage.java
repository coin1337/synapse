
package com.ragemachine.moon.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventStage extends Event {
    private int stage;

    private State state = State.PRE;

    public EventStage() {
    }

    public enum State {
        PRE, POST
    }


    public EventStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }

    public State getEventState() {
        return state;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}

