package com.ragemachine.moon.api.event.events;

public class EventPlayerTravel extends EventCancellable
{
    public float Strafe;
    public float Vertical;
    public float Forward;
    
    public EventPlayerTravel(final float p_Strafe, final float p_Vertical, final float p_Forward) {
        super();
        this.Strafe = p_Strafe;
        this.Vertical = p_Vertical;
        this.Forward = p_Forward;
    }
}