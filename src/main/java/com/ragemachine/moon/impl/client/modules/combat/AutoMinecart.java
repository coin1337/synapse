package com.ragemachine.moon.impl.client.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.InventoryUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;

public class AutoMinecart extends Module {
   private final Value silent;
   public Value minHP;
   private Value delay;
   private final Value packet;
   int wait;
   int waitFlint;
   int originalSlot;

   public AutoMinecart() {
      super("AutoMinecart", "Places and explodes minecarts on other players.", Category.COMBAT, true, false, false);
      this.silent = this.addSetting(new Value("Silent", Boolean.FALSE));
      this.minHP = this.addSetting(new Value("Min Health", 4.0F, 0.0F, 36.0F));
      this.delay = this.addSetting(new Value("Wait", 20, 0, 50));
      this.packet = this.addSetting(new Value("PacketPlace", Boolean.FALSE));
   }

   public void onEnable() {
      if (fullNullCheck()) {
         this.toggle();
      }

      this.wait = 0;
      this.waitFlint = 0;
      this.originalSlot = mc.player.inventory.currentItem;
   }

   public void onTick() {
      if (fullNullCheck()) {
         this.toggle();
      }

      int tntSlot = InventoryUtil.getItemHotbar(Items.TNT_MINECART);
      int flintSlot = InventoryUtil.getItemHotbar(Items.FLINT_AND_STEEL);
      int railSlot = InventoryUtil.findHotbarBlock(Blocks.ACTIVATOR_RAIL);
      int picSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
      if (tntSlot == -1 || railSlot == -1 || flintSlot == -1 || picSlot == -1) {
         Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No (tnt minecart/activator rail/flint/pic) in hotbar disabling...");
         this.toggle();
      }

      EntityPlayer target = this.getTarget();
      if (target != null) {
         BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
         Vec3d hitVec = (new Vec3d(pos)).add(0.5D, -0.5D, 0.5D);
         if (mc.player.getDistanceSq(pos) <= MathUtil.square(6.0D)) {
            if (mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
               if (this.wait == 0) {
                  InventoryUtil.switchToHotbarSlot(railSlot, (Boolean)this.silent.getValue());
                  BlockUtil.rightClickBlock(pos.down(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
               }

               int i;
               if (this.wait == 1) {
                  InventoryUtil.switchToHotbarSlot(tntSlot, (Boolean)this.silent.getValue());

                  for(i = 0; i < 15; ++i) {
                     BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
                  }
               }

               if (this.wait == 2) {
                  InventoryUtil.switchToHotbarSlot(tntSlot, (Boolean)this.silent.getValue());

                  for(i = 0; i < 15; ++i) {
                     BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
                  }
               }

               if (this.wait == 3) {
                  InventoryUtil.switchToHotbarSlot(tntSlot, (Boolean)this.silent.getValue());

                  for(i = 0; i < 15; ++i) {
                     BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
                  }
               }

               if (this.wait == 4) {
                  InventoryUtil.switchToHotbarSlot(tntSlot, (Boolean)this.silent.getValue());

                  for(i = 0; i < 15; ++i) {
                     BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
                  }
               }
            } else {
               this.wait = 0;
               if (mc.world.getBlockState(pos).getBlock() == Blocks.ACTIVATOR_RAIL) {
                  InventoryUtil.switchToHotbarSlot(picSlot, (Boolean)this.silent.getValue());
                  mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
               }

               if (mc.world.getBlockState(pos).getBlock().getBlockState().getBaseState().getMaterial() != Material.FIRE) {
                  InventoryUtil.switchToHotbarSlot(flintSlot, (Boolean)this.silent.getValue());
                  BlockUtil.rightClickBlock(pos.down(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
               }
            }

            if (this.wait < (Integer)this.delay.getValue()) {
               ++this.wait;
               return;
            }

            this.wait = 0;
            InventoryUtil.switchToHotbarSlot(picSlot, (Boolean)this.silent.getValue());
            mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
            InventoryUtil.switchToHotbarSlot(flintSlot, (Boolean)this.silent.getValue());
            BlockUtil.rightClickBlock(pos.down(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, (Boolean)this.packet.getValue());
         }

      }
   }

   public void onDisable() {
      mc.player.inventory.currentItem = this.originalSlot;
   }

   private EntityPlayer getTarget() {
      EntityPlayer target = null;
      double distance = Math.pow(6.0D, 2.0D) + 1.0D;
      Iterator var4 = mc.world.playerEntities.iterator();

      while(var4.hasNext()) {
         EntityPlayer player = (EntityPlayer)var4.next();
         if (!EntityUtil.isntValid(player, 6.0D) && !player.isInWater() && !player.isInLava() && EntityUtil.isTrapped(player, false, false, false, false, false) && player.getHealth() + player.getAbsorptionAmount() <= (Float)this.minHP.getValue()) {
            if (target == null) {
               target = player;
               distance = mc.player.getDistanceSq(player);
            } else if (mc.player.getDistanceSq(player) < distance) {
               target = player;
               distance = mc.player.getDistanceSq(player);
            }
         }
      }

      return target;
   }
}
