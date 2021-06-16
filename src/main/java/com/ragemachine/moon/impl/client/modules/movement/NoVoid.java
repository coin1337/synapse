package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.impl.client.modules.Module;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

public class NoVoid extends Module {
   public NoVoid() {
      super("AntiVoid", "Glitches you up from void.", Category.MOVEMENT, false, false, false);
   }

   public void onUpdate() {
      if (!fullNullCheck()) {
         if (!mc.player.noClip && mc.player.posY <= 0.0D) {
            RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0D, mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == Type.BLOCK) {
               return;
            }

            mc.player.setVelocity(0.0D, 0.0D, 0.0D);
            if (mc.player.getRidingEntity() != null) {
               mc.player.getRidingEntity().setVelocity(0.0D, 0.0D, 0.0D);
            }
         }

      }
   }
}
