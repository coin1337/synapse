package com.ragemachine.moon.impl.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class SynapseTessellator extends Tessellator {
   public static SynapseTessellator INSTANCE = new SynapseTessellator();

   public SynapseTessellator() {
      super(2097152);
   }

   public static void prepare(int mode) {
      prepareGL();
      begin(mode);
   }

   public static void prepareGL() {
      GL11.glBlendFunc(770, 771);
      GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
      GlStateManager.glLineWidth(1.5F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F);
   }

   public static void begin(int mode) {
      INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
   }

   public static void release() {
      render();
      releaseGL();
   }

   public static void render() {
      INSTANCE.draw();
   }

   public static void releaseGL() {
      GlStateManager.enableCull();
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.enableDepth();
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawBox(AxisAlignedBB bb, int argb, int sides) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBox(INSTANCE.getBuffer(), bb, r, g, b, a, sides);
   }

   public static void drawBox(BlockPos blockPos, int argb, int sides) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBox(blockPos, r, g, b, a, sides);
   }

   public static void drawHalfBox(BlockPos blockPos, int argb, int sides) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawHalfBox(blockPos, r, g, b, a, sides);
   }

   public static void drawHalfBox(float x, float y, float z, int argb, int sides) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBox(INSTANCE.getBuffer(), x, y, z, 1.0F, 0.5F, 1.0F, r, g, b, a, sides);
   }

   public static void drawBox(float x, float y, float z, int argb, int sides) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBox(INSTANCE.getBuffer(), x, y, z, 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
   }

   public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
      drawBox(INSTANCE.getBuffer(), (float)blockPos.getX(), (float)blockPos.getY(), (float)blockPos.getZ(), 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
   }

   public static void drawHalfBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
      drawBox(INSTANCE.getBuffer(), (float)blockPos.getX(), (float)blockPos.getY(), (float)blockPos.getZ(), 1.0F, 0.5F, 1.0F, r, g, b, a, sides);
   }

   public static void drawBox(Vec3d vec3d, int r, int g, int b, int a, int sides) {
      drawBox(INSTANCE.getBuffer(), (float)vec3d.x, (float)vec3d.y, (float)vec3d.z, 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
   }

   public static BufferBuilder getBufferBuilder() {
      return INSTANCE.getBuffer();
   }

   public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
      if ((sides & 1) != 0) {
         buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 2) != 0) {
         buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 4) != 0) {
         buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 8) != 0) {
         buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 16) != 0) {
         buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 32) != 0) {
         buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

   }

   public static void drawBox(BufferBuilder buffer, AxisAlignedBB bb, int r, int g, int b, int a, int sides) {
      if ((sides & 1) != 0) {
         buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
      }

      if ((sides & 2) != 0) {
         buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
      }

      if ((sides & 4) != 0) {
         buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
      }

      if ((sides & 8) != 0) {
         buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
      }

      if ((sides & 16) != 0) {
         buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
      }

      if ((sides & 32) != 0) {
         buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
         buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
      }

   }

   public static void drawSmallBox(Vec3d vec3d, int r, int g, int b, int a, int sides) {
      drawBox(INSTANCE.getBuffer(), (float)vec3d.x, (float)vec3d.y, (float)vec3d.z, 0.3F, 0.3F, 0.3F, r, g, b, a, sides);
   }

   public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
      if ((sides & 17) != 0) {
         buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 18) != 0) {
         buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 33) != 0) {
         buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 34) != 0) {
         buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 5) != 0) {
         buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 6) != 0) {
         buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 9) != 0) {
         buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 10) != 0) {
         buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 20) != 0) {
         buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 36) != 0) {
         buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
      }

      if ((sides & 24) != 0) {
         buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

      if ((sides & 40) != 0) {
         buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
         buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
      }

   }

   public static void drawBoundingBox(AxisAlignedBB bb, float width, int argb) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBoundingBox(bb, width, r, g, b, a);
   }

   public static void drawBoundingBox(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha) {
      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      GlStateManager.glLineWidth(width);
      BufferBuilder bufferbuilder = INSTANCE.getBuffer();
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
      bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
      render();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   public static void drawBoundingBoxBottom(AxisAlignedBB bb, float width, int argb) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBoundingBoxBottom(bb, width, r, g, b, a);
   }

   public static void drawBoundingBoxBottom(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha) {
      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      GlStateManager.glLineWidth(width);
      BufferBuilder bufferbuilder = INSTANCE.getBuffer();
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
      bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      render();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   public static void drawBoundingBoxHalf(AxisAlignedBB bb, float width, int argb) {
      int a = argb >>> 24 & 255;
      int r = argb >>> 16 & 255;
      int g = argb >>> 8 & 255;
      int b = argb & 255;
      drawBoundingBoxHalf(bb, width, r, g, b, a);
   }

   public static void drawBoundingBoxHalf(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha) {
      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      GlStateManager.glLineWidth(width);
      BufferBuilder bufferbuilder = INSTANCE.getBuffer();
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
      bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY - 0.5D, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY - 0.5D, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY - 0.5D, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY - 0.5D, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY - 0.5D, bb.maxZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY - 0.5D, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.maxX, bb.maxY - 0.5D, bb.minZ).color(red, green, blue, alpha).endVertex();
      bufferbuilder.pos(bb.minX, bb.maxY - 0.5D, bb.minZ).color(red, green, blue, alpha).endVertex();
      render();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }
}
