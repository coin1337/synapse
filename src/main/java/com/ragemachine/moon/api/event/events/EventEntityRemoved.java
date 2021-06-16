package com.ragemachine.moon.api.event.events;

import net.minecraft.entity.Entity;

public class EventEntityRemoved extends EventCancellable {

    private final Entity entity;

    public EventEntityRemoved(Entity entity) {
        this.entity = entity;
    }

    public Entity get_entity() {
        return this.entity;
    }

}