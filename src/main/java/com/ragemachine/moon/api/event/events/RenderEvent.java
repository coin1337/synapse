package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.MoonEvent;

public class RenderEvent extends MoonEvent {
		private final float partialTicks;

		public RenderEvent(float ticks){
			super();
			partialTicks = ticks;
		}

		public float getPartialTicks(){
			return partialTicks;
		}
}
