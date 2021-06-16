package com.ragemachine.moon.api.event.events;

/**
 * Author Seth
 * 4/6/2019 @ 1:27 AM.
 */

public class EventCancellable2 extends EventStageable {

    private boolean canceled;

    public EventCancellable2() {
    }

    public EventCancellable2(EventStage stage) {
        super(stage);
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}