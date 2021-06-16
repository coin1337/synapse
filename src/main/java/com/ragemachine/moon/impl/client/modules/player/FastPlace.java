package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.InventoryUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class FastPlace extends Module {

    private Value<Boolean> all = addSetting(new Value("All", false));
    private Value<Boolean> obby = addSetting(new Value("Obsidian", false, v -> !all.getValue()));
    private Value<Boolean> enderChests = addSetting(new Value("EnderChests", false, v -> !all.getValue()));
    private Value<Boolean> crystals = addSetting(new Value("Crystals", false, v -> !all.getValue()));
    private Value<Boolean> exp = addSetting(new Value("Experience", false, v -> !all.getValue()));
    private Value<Boolean> fastCrystal = addSetting(new Value("PacketCrystal", false));

    private BlockPos mousePos = null;

    public FastPlace() {
        super("FastPlace", "Fast everything.", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if(fullNullCheck()) {
            return;
        }

        if (InventoryUtil.holdingItem(ItemExpBottle.class) && exp.getValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (InventoryUtil.holdingItem(BlockObsidian.class) && obby.getValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (InventoryUtil.holdingItem(BlockEnderChest.class) && enderChests.getValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (all.getValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (InventoryUtil.holdingItem(ItemEndCrystal.class) && (crystals.getValue() || all.getValue())) {
            mc.rightClickDelayTimer = 0;
        }

        if(fastCrystal.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            if(offhand || mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                final RayTraceResult result = mc.objectMouseOver;
                if (result == null) {
                    return;
                }

                switch (result.typeOfHit) {
                    case MISS:
                        mousePos = null;
                        break;
                    case BLOCK:
                        mousePos = mc.objectMouseOver.getBlockPos();
                        break;
                    case ENTITY:
                        if(mousePos != null) {
                            Entity entity = result.entityHit;
                            if(entity != null) {
                                if(mousePos.equals(new BlockPos(entity.posX, entity.posY - 1, entity.posZ))) {
                                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                                }
                            }
                        }
                        break;
                }
            }
        }
    }
}
