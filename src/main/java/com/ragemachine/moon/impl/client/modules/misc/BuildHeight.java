package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BuildHeight extends Module {

    private Value<Integer> height = addSetting(new Value("Height", 255, 0, 255));

    public BuildHeight() {
        super("BuildHeight", "Allows you to place at build height", Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if(event.getStage() == 0) {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
                if(packet.getPos().getY() >= height.getValue() && packet.getDirection() == EnumFacing.UP) {
                    packet.placedBlockDirection = EnumFacing.DOWN;
                }
            }
        }
    }
}
