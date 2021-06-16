package com.ragemachine.moon.api.event.events;

import net.minecraft.network.Packet;
public class EventPacket extends EventCancellable {
	private static Packet packet2;
	private final Packet packet;

	public EventPacket(Packet packet) {
		this.packet = (Packet) packet2;
	}

	public static Object getPacket() {
		return packet2;
	}

	public Packet get_packet() {
		return this.packet;
	}

	public static class SendPacket extends EventPacket {
		public SendPacket(Packet packet) {
			super(packet);
		}
	}

	public static class ReceivePacket extends EventPacket {
		public ReceivePacket(Packet packet) {
			super(packet);
		}
	}
}