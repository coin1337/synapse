package com.ragemachine.moon.impl.client.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Iterator;

public class PearlNotify extends Module {

   //Oy
   private Entity enderPearl;
   private boolean flag;
   private final HashMap list = new HashMap();

   public PearlNotify() {
      super("PearlNotify", "Notify pearl throws.", Category.MISC, true, false, false);
   }

   public void onEnable() {
      this.flag = true;
   }

   public void onUpdate() {
      if (mc.world != null && mc.player != null) {
         this.enderPearl = null;
         Iterator var1 = mc.world.loadedEntityList.iterator();

         while(var1.hasNext()) {
            Entity e = (Entity)var1.next();
            if (e instanceof EntityEnderPearl) {
               this.enderPearl = e;
               break;
            }
         }

         if (this.enderPearl == null) {
            this.flag = true;
         } else {
            EntityPlayer closestPlayer = null;
            Iterator var5 = mc.world.playerEntities.iterator();

            while(var5.hasNext()) {
               EntityPlayer entity = (EntityPlayer)var5.next();
               if (closestPlayer == null) {
                  closestPlayer = entity;
               } else if (closestPlayer.getDistance(this.enderPearl) > entity.getDistance(this.enderPearl)) {
                  closestPlayer = entity;
               }
            }

            if (closestPlayer == mc.player) {
               this.flag = false;
            }

            if (closestPlayer != null && this.flag) {
               String faceing = this.enderPearl.getHorizontalFacing().toString();
               if (faceing.equals("west")) {
                  faceing = "east";
               } else if (faceing.equals("east")) {
                  faceing = "west";
               }

               Command.sendMessage(MoonHack.getFriendManager.isFriend(closestPlayer.getName()) ? ChatFormatting.AQUA + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!" : ChatFormatting.RED + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!");
               this.flag = false;
            }

         }
      }
   }
}
