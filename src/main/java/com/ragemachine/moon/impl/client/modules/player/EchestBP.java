package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.impl.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;

public class EchestBP extends Module {

    //Thanks BadLion+, probs will remove this module later since I don't like it. May also try to improve it idk.

    private GuiScreen echestScreen = null;

    public EchestBP() {
        super("EchestBackpack", "Allows to open your echest later.", Category.PLAYER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if(mc.currentScreen instanceof GuiContainer) {
            Container container = ((GuiContainer)mc.currentScreen).inventorySlots;
            if(container instanceof ContainerChest && ((ContainerChest)container).getLowerChestInventory() instanceof InventoryBasic) {
                InventoryBasic basic = (InventoryBasic) ((ContainerChest)container).getLowerChestInventory();
                if(basic.getName().equalsIgnoreCase("Ender Chest")) {
                    echestScreen = mc.currentScreen;
                    mc.currentScreen = null;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if(!fullNullCheck() && echestScreen != null) {
            mc.displayGuiScreen(echestScreen);
        }
        echestScreen = null;
    }
}
