package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import java.util.Iterator;

public class Offhand extends Module {
   private boolean dragging = false;
   private int totems = 0;
   private Value<Integer> health = this.addSetting(new Value("Health", 15, 0, 40));
   private Value<Boolean> totemOnDisable = this.addSetting(new Value("TotemOnDisable", false));
   private Value<Boolean> disableOthers = this.addSetting(new Value("DisableOthers", false));
   private Value<Mode> mode = addSetting(new Value("Type", Mode.GoldenApple));

   public Offhand() {
      super("Offhand", "OffhandGapples", Category.COMBAT, true, false, true);
   }

   public String getHudInfo() {
      return this.totems + "";
   }

   public void onEnable() {
      if ((Boolean)this.disableOthers.getValue()) {
         AutoTotem autoTotem = (AutoTotem) MoonHack.getModuleManager.getModuleByName("AutoTotem");
         if (autoTotem.isEnabled()) {
            autoTotem.disable();
         }
      }

   }

   public void onDisable() {
      AutoTotem autoTotem = (AutoTotem)MoonHack.getModuleManager.getModuleByName("AutoTotem");
      if ((Boolean)this.totemOnDisable.getValue()) {
      }

      autoTotem.enable();
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (!this.isEnabled()) {
         if ((Boolean)this.totemOnDisable.getValue() && !(Wrapper.mc.currentScreen instanceof GuiContainer) && Wrapper.getPlayer() != null) {
            int i;
            int slot;
            if (!Wrapper.getPlayer().inventory.getItemStack().isEmpty() && !this.dragging) {
               for(i = 0; i < 45; ++i) {
                  if (Wrapper.getPlayer().inventory.getStackInSlot(i).isEmpty() || Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() == Items.AIR) {
                     slot = i < 9 ? i + 36 : i;
                     Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Wrapper.getPlayer());
                     return;
                  }
               }
            }

            if (Wrapper.getPlayer().getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
               MinecraftForge.EVENT_BUS.unregister(this);
               this.dragging = false;
            } else if (this.dragging) {
               Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Wrapper.getPlayer());
               this.dragging = false;
            } else {
               for(i = 0; i < 45; ++i) {
                  if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                     slot = i < 9 ? i + 36 : i;
                     Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Wrapper.getPlayer());
                     this.dragging = true;
                     return;
                  }
               }

            }
         } else {
            MinecraftForge.EVENT_BUS.unregister(this);
         }
      }
   }

   public void onUpdate() {
      if (!(Wrapper.mc.currentScreen instanceof GuiContainer)) {
         EntityPlayerSP player = Wrapper.getPlayer();
         if (player != null) {
            int i;
            if (!player.inventory.getItemStack().isEmpty() && !this.dragging) {
               for(int i2 = 0; i2 < 45; ++i2) {
                  if (player.inventory.getStackInSlot(i2).isEmpty() || player.inventory.getStackInSlot(i2).getItem() == Items.AIR) {
                     i = i2 < 9 ? i2 + 36 : i2;
                     Wrapper.mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, player);
                     return;
                  }
               }
            }

            this.totems = player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING ? player.getHeldItemOffhand().getCount() : 0;
            Iterator var5 = player.inventory.mainInventory.iterator();

            while(var5.hasNext()) {
               ItemStack stack = (ItemStack)var5.next();
               if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                  this.totems += stack.getCount();
               }
            }

            Item itemG = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.GOLDEN_APPLE;
            Item itemC = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.END_CRYSTAL;
            Item itemB = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.BOW;
            Item itemS = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.SHIELD;
            Item itemP = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.POTIONITEM;
            Item itemZ = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.SPLASH_POTION;

             if (mode.getValue() == Mode.GoldenApple) {
                if (player.getHeldItemOffhand().getItem() != itemG) {
                   if (this.dragging) {
                      Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                      this.dragging = false;
                   } else {
                      for (i = 0; i < 45; ++i) {
                         if (player.inventory.getStackInSlot(i).getItem() == itemG) {
                            int slot = i < 9 ? i + 36 : i;
                            Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                            this.dragging = true;
                            return;
                         }
                      }
                   }
                }
             }
            if (mode.getValue() == Mode.EndCrystal) {
               if (player.getHeldItemOffhand().getItem() != itemC) {
                  if (this.dragging) {
                     Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                     this.dragging = false;
                  } else {
                     for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == itemC) {
                           int slot = i < 9 ? i + 36 : i;
                           Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                           this.dragging = true;
                           return;
                        }
                     }
                  }
               }
            }
            if (mode.getValue() == Mode.Bow) {
               if (player.getHeldItemOffhand().getItem() != itemB) {
                  if (this.dragging) {
                     Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                     this.dragging = false;
                  } else {
                     for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == itemB) {
                           int slot = i < 9 ? i + 36 : i;
                           Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                           this.dragging = true;
                           return;
                        }
                     }
                  }
               }
            }
            if (mode.getValue() == Mode.Shield) {
               if (player.getHeldItemOffhand().getItem() != itemS) {
                  if (this.dragging) {
                     Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                     this.dragging = false;
                  } else {
                     for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == itemS) {
                           int slot = i < 9 ? i + 36 : i;
                           Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                           this.dragging = true;
                           return;
                        }
                     }
                  }
               }
            }
            if (mode.getValue() == Mode.StrengthPot) {
               if (player.getHeldItemOffhand().getItem() != itemP) {
                  if (this.dragging) {
                     Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                     this.dragging = false;
                  } else {
                     for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == itemP) {
                           int slot = i < 9 ? i + 36 : i;
                           Wrapper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                           this.dragging = true;
                           return;
                        }
                     }
                  }
               }
            }
            if (mode.getValue() == Mode.SplashPot) {
               if (player.getHeldItemOffhand().getItem() != itemZ) {
                  if (this.dragging) {
                     Wrapper.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                     this.dragging = false;
                  } else {
                     for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == itemZ) {
                           int slot = i < 9 ? i + 36 : i;
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

   private boolean shouldTotem() {
      boolean hp = Wrapper.getPlayer().getHealth() + Wrapper.getPlayer().getAbsorptionAmount() <= (float)(Integer)this.health.getValue();
      boolean totemCount = this.totems > 0 || this.dragging || !Wrapper.getPlayer().inventory.getItemStack().isEmpty();
      return hp && totemCount;
   }

   public enum Mode {
      GoldenApple,
      EndCrystal,
      Bow,
      Shield,
      StrengthPot,
      SplashPot,
   }
}
