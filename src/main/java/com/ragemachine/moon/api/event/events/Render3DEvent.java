
package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.EventStage;

public class Render3DEvent extends EventStage {
    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

