
package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.api.event.EventStage;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClientEvent
extends EventStage {
    private Hack hack;
    private Value value;

    public ClientEvent(int stage, Hack hack) {
        super(stage);
        this.hack = hack;
    }

    public ClientEvent(Value value) {
        super(2);
        this.value = value;
    }

    public Hack getFeature() {
        return this.hack;
    }

    public Value getSetting() {
        return this.value;
    }
}

