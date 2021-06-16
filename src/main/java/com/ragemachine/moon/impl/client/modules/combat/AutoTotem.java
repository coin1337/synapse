package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class AutoTotem extends Module {
    private Value soft = this.addSetting(new Value("Soft", false));
    private Value disableOthers = this.addSetting(new Value("DisableOthers", false));
    private boolean dragging = false;
    private int totems = 0;

    public AutoTotem() {
        super("AutoTotem", "Switches A Totem Into Your Hand", Category.COMBAT, true, false, true);
    }

    public String getHudInfo() {
        return this.totems + "";
    }

    public void onEnable() {
        if ((Boolean)this.disableOthers.getValue()) {
            Offhand offhand = (Offhand) MoonHack.getModuleManager.getModuleByName("Offhand");

            if (offhand.isEnabled()) {
                offhand.disable();
            }
        }

    }

    public void onUpdate() {
        if (!(Wrapper.mc.currentScreen instanceof GuiContainer)) {
            EntityPlayerSP player = Wrapper.getPlayer();
            if (player != null) {
                int i;
                int slot;
                if (!player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for(i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).isEmpty() || player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
                            slot = i < 9 ? i + 36 : i;
                            Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                            return;
                        }
                    }
                }

                this.totems = 0;
                Iterator var4 = player.inventory.mainInventory.iterator();

                while(var4.hasNext()) {
                    ItemStack stack = (ItemStack)var4.next();
                    if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                        this.totems += stack.getCount();
                    }
                }

                if (player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    this.totems += player.getHeldItemOffhand().getCount();
                } else if (!(Boolean)this.soft.getValue() || player.getHeldItemOffhand().isEmpty()) {
                    if (this.dragging) {
                        Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                        this.dragging = false;
                    } else {
                        for(i = 0; i < 45; ++i) {
                            if (player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                                slot = i < 9 ? i + 36 : i;
                                Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                                this.dragging = true;
                                return;
                            }
                        }

                    }
                }
            }
        }
    }
}
