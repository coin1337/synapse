package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.SynapseTessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ESP extends Module {

   public Value<Mode> mode = addSetting(new Value("Mode", Mode.CSGO));
   public Value<Boolean> players = addSetting(new Value("Players", true));
   public Value<Boolean> passive = addSetting(new Value("Passive", false));
   public Value<Boolean> monsters = addSetting(new Value("Hostile", false));
   public Value<Boolean> items = addSetting(new Value("Items", false));
   public Value<Boolean> crystals = addSetting(new Value("Crystals", false));
   
   public Value<Float> red = addSetting(new Value("Player Enemy Red", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   public Value<Float> green = addSetting(new Value("Player Enemy Green", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   public Value<Float> blue = addSetting(new Value("Player Enemy Blue", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   public final Value<Float> alpha = addSetting(new Value("Player Enemy Alpha", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   
   public Value<Float> fRed = addSetting(new Value("Friends Red", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   public Value<Float> fGreen = addSetting(new Value("Friends Green", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   public Value<Float> fBlue = addSetting(new Value("Friends Blue", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   public final Value<Float> fAlpha = addSetting(new Value("Friends Alpha", 255.0f, 0.1f, 255.0f, v -> players.getValue()));
   
   public Value<Float> pRed = addSetting(new Value("Passive Red", 255.0f, 0.1f, 255.0f, v -> passive.getValue()));
   public Value<Float>pGreen = addSetting(new Value("Passive Green", 255.0f, 0.1f, 255.0f, v -> passive.getValue()));
   public Value<Float> pBlue = addSetting(new Value("Passive Blue", 255.0f, 0.1f, 255.0f, v -> passive.getValue()));
   public final Value<Float> pAlpha = addSetting(new Value("Passive Alpha", 255.0f, 0.1f, 255.0f, v -> passive.getValue()));

   public Value<Float> hRed = addSetting(new Value("Hostile Red", 255.0f, 0.1f, 255.0f, v -> monsters.getValue()));
   public Value<Float> hGreen = addSetting(new Value("Hostile Green", 255.0f, 0.1f, 255.0f, v -> monsters.getValue()));
   public Value<Float> hBlue = addSetting(new Value("Hostile Blue", 255.0f, 0.1f, 255.0f, v -> monsters.getValue()));
   public final Value<Float> hAlpha = addSetting(new Value("Hostile Alpha", 255.0f, 0.1f, 255.0f, v -> monsters.getValue()));
   
   public Value<Float> iRed = addSetting(new Value("Items Red", 255.0f, 0.1f, 255.0f, v -> items.getValue()));
   public Value<Float> iGreen = addSetting(new Value("Items Green", 255.0f, 0.1f, 255.0f, v -> items.getValue()));
   public Value<Float> iBlue = addSetting(new Value("Items Blue", 255.0f, 0.1f, 255.0f, v -> items.getValue()));
   public final Value<Float> iAlpha = addSetting(new Value("Items Alpha", 255.0f, 0.1f, 255.0f, v -> items.getValue()));
   
   public Value<Float> cRed = addSetting(new Value("Crystal Red", 255.0f, 0.1f, 255.0f, v -> crystals.getValue()));
   public Value<Float> cGreen = addSetting(new Value("Crystal Green", 255.0f, 0.1f, 255.0f, v -> crystals.getValue()));
   public Value<Float> cBlue = addSetting(new Value("Crystal Blue", 255.0f, 0.1f, 255.0f, v -> crystals.getValue()));
   public final Value<Float> cAlpha = addSetting(new Value("Crystal Alpha", 255.0f, 0.1f, 255.0f, v -> crystals.getValue()));



   public ESP() {
      super("ESP", "Based ESP", Category.RENDER, true, false, true);
   }

   public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
      return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
      return getInterpolatedAmount(entity, ticks, ticks, ticks);
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
      return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
   }

   @Override
   public void onRender3D(Render3DEvent event) {
      if (mode.getValue() == Mode.CSGO) {
         if (mc.getRenderManager().options != null) {
            boolean isThirdPersonFrontal = mc.getRenderManager().options.thirdPersonView == 2;
            float viewerYaw = mc.getRenderManager().playerViewY;
            mc.world.loadedEntityList.stream().filter((entity) -> {
               return mc.player != entity;
            }).forEach((e) -> {
               SynapseTessellator.prepareGL();
               GlStateManager.pushMatrix();
               Vec3d pos = getInterpolatedPos(e, mc.getRenderPartialTicks());
               GlStateManager.translate(pos.x - mc.getRenderManager().renderPosX, pos.y - mc.getRenderManager().renderPosY, pos.z - mc.getRenderManager().renderPosZ);
               GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
               GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
               GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
               GL11.glLineWidth(3.0F);
               GL11.glEnable(2848);
               if (e instanceof EntityPlayer && players.getValue()) {
                  if (MoonHack.getFriendManager.isFriend(e.getName()) && players != null) {
                     GL11.glColor4f(fRed.getValue() / 255, fGreen.getValue() / 255, fBlue.getValue() / 255, fAlpha.getValue());
                  } else {
                     GL11.glColor4f(red.getValue() / 255, green.getValue() / 255, blue.getValue() / 255, alpha.getValue());
                  }

                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(pRed.getValue() / 255, pGreen.getValue() / 255, pBlue.getValue() / 255, pAlpha.getValue());
               if (EntityUtil.isPassive(e) && passive.getValue() && passive != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(hRed.getValue() / 255, hGreen.getValue() / 255, hBlue.getValue() / 255, hAlpha.getValue());
               if (EntityUtil.isHostileMob(e) && monsters.getValue() && monsters != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(iRed.getValue() / 255, iGreen.getValue() / 255, iBlue.getValue() / 255, iAlpha.getValue());
               if (e instanceof EntityItem && items.getValue() && items != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(iRed.getValue() / 255, iGreen.getValue() / 255, iBlue.getValue() / 255, iAlpha.getValue());
               if (e instanceof EntityEnderPearl && items.getValue() && items != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(iRed.getValue() / 255, iGreen.getValue() / 255, iBlue.getValue() / 255, iAlpha.getValue());
               if (e instanceof EntityXPOrb && items.getValue() && items != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(iRed.getValue() / 255, iGreen.getValue() / 255, iBlue.getValue() / 255, iAlpha.getValue());
               if (e instanceof EntityExpBottle && items.getValue() && items != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d((-e.width), e.height);
                  GL11.glVertex2d((-e.width), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), e.height);
                  GL11.glVertex2d(e.width, e.height);
                  GL11.glVertex2d(e.width, (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F), 0.0D);
                  GL11.glVertex2d(e.width, 0.0D);
                  GL11.glVertex2d(e.width, (e.height / 3.0F));
                  GL11.glEnd();
               }

               GL11.glColor4f(cRed.getValue() / 255, cGreen.getValue() / 255, cBlue.getValue() / 255, cAlpha.getValue());
               if (e instanceof EntityEnderCrystal && crystals.getValue() && crystals != null) {
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width / 2.0F), 0.0D);
                  GL11.glVertex2d((-e.width / 2.0F), (e.height / 3.0F));
                  GL11.glVertex2d((-e.width / 2.0F), 0.0D);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F / 2.0F), 0.0D);
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((-e.width / 2.0F), e.height);
                  GL11.glVertex2d((-e.width / 3.0F * 2.0F / 2.0F), e.height);
                  GL11.glVertex2d((-e.width / 2.0F), e.height);
                  GL11.glVertex2d((-e.width / 2.0F), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((e.width / 2.0F), e.height);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F / 2.0F), e.height);
                  GL11.glVertex2d((e.width / 2.0F), e.height);
                  GL11.glVertex2d((e.width / 2.0F), (e.height / 3.0F * 2.0F));
                  GL11.glEnd();
                  GL11.glBegin(2);
                  GL11.glVertex2d((e.width / 2.0F), 0.0D);
                  GL11.glVertex2d((e.width / 3.0F * 2.0F / 2.0F), 0.0D);
                  GL11.glVertex2d((e.width / 2.0F), 0.0D);
                  GL11.glVertex2d((e.width / 2.0F), (e.height / 3.0F));
                  GL11.glEnd();
               }


               SynapseTessellator.releaseGL();
               GlStateManager.popMatrix();
            });
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }
   }

   public enum Mode {
      CSGO,
      SHADER;
   }
}
