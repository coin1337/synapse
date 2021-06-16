package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Iterator;

public class PacketEXP extends Module {
    public Value<Boolean> throwExp;
    public Value<Boolean> autoMend;
    public Value<Bind> xpBind;

    public PacketEXP() {
        super("PacketEXP", "Throws EXP at your feet", Category.COMBAT, false, false, true);
        xpBind = addSetting(new Value("Throw Bind", new Bind(-1)));
        autoMend = addSetting(new Value("Automatic", false));


    }

    public void onUpdate() {
        Item itemMainHand = mc.player.getHeldItemMainhand().getItem();
        Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();
        boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
        boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;
        int ArmorDurability = this.getArmorDurability();

        if (autoMend.getValue()) {
            mc.rightClickDelayTimer = 0;
            mc.player.inventory.currentItem = this.findExpInHotbar();
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0.0F, 90.0F, true));
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
            super.onUpdate();
        }
        else if (xpBind.getValue().isDown()) {
            mc.rightClickDelayTimer = 0;
            mc.player.inventory.currentItem = this.findExpInHotbar();
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0.0F, 90.0F, true));
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
            super.onUpdate();
        }
        else if (!autoMend.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            mc.rightClickDelayTimer = 0;
            mc.player.inventory.currentItem = this.findExpInHotbar();
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0.0F, 90.0F, true));
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
            super.onUpdate();
        }
    }



    private int getArmorDurability() {
        int TotalDurability = 0;

        ItemStack itemStack;
        for(Iterator var2 = mc.player.inventory.armorInventory.iterator(); var2.hasNext(); TotalDurability += itemStack.getItemDamage()) {
            itemStack = (ItemStack)var2.next();
        }

        return TotalDurability;
    }

    private int findExpInHotbar() {
        int slot = 0;

        for(int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }

        return slot;
    }

}
