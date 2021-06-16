package com.ragemachine.moon.api.event.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvent2 extends Event {
  
  private final Packet<?> packet;
  
  public PacketEvent2(Packet<?> packetIn) {
    packet = packetIn;
  }
  
  public <T extends Packet<?>> T getPacket() {
    return (T) packet;
  }

  public static class Outgoing extends PacketEvent2 {

    public Outgoing(Packet<?> packetIn) {
      super(packetIn);
    }

    @Cancelable
    public static class Pre extends Outgoing {

      public Pre(Packet<?> packetIn) {
        super(packetIn);
      }
    }

    public static class Post extends Outgoing {

      public Post(Packet<?> packetIn) {
        super(packetIn);
      }
    }
  }

  public static class Incoming extends PacketEvent2 {

    public Incoming(Packet<?> packetIn) {
      super(packetIn);
    }

    @Cancelable
    public static class Pre extends Incoming {

      public Pre(Packet<?> packetIn) {
        super(packetIn);
      }
    }

    public static class Post extends Incoming {

      public Post(Packet<?> packetIn) {
        super(packetIn);
      }
    }
  }
}
