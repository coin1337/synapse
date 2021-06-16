package com.ragemachine.moon.impl.util;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.GL_QUADS;

import java.awt.Color;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;


public class RenderUtils {
	/**
	 * Renders a box with any size and any color.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param color
	 */
	public static void box(double x, double y, double z, double x2, double y2, double z2, float red, float green,
			float blue, float alpha) {
		x = x - ReflectionFields.getRenderPosX();
		y = y - ReflectionFields.getRenderPosY();
		z = z - ReflectionFields.getRenderPosZ();
		x2 = x2 - ReflectionFields.getRenderPosX();
		y2 = y2 - ReflectionFields.getRenderPosY();
		z2 = z2 - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawColorBox(new AxisAlignedBB(x, y, z, x2, y2, z2), red, green, blue, alpha);
		GL11.glColor4d(0, 0, 0, 0.5F);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	
	public static void LogOutBox(double x, double y, double z, double x2, double y2, double z2, Color color) {
		x = x - ReflectionFields.getRenderPosX();
		y = y - ReflectionFields.getRenderPosY();
		z = z - ReflectionFields.getRenderPosZ();
		x2 = x2 - ReflectionFields.getRenderPosX();
		y2 = y2 - ReflectionFields.getRenderPosY();
		z2 = z2 - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDepthMask(false);
		setColor(color);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	/**
	 * Renders a frame with any size and any color.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param color
	 */
	public static void frame(double x, double y, double z, double x2, double y2, double z2, Color color) {
		x = x - ReflectionFields.getRenderPosX();
		y = y - ReflectionFields.getRenderPosY();
		z = z - ReflectionFields.getRenderPosZ();
		x2 = x2 - ReflectionFields.getRenderPosX();
		y2 = y2 - ReflectionFields.getRenderPosY();
		z2 = z2 - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		setColor(color);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	/**
	 * Renders a green ESP box with the size of a normal block at the specified
	 * BlockPos.
	 */

	public static void blockESP(BlockPos b, Color c, double length, double length2) {
		blockEsp(b, c, length, length2);
	}

	/**
	 * Renders an ESP box with the size of a normal block at the specified BlockPos.
	 */
	public static void blockEsp(BlockPos blockPos, Color c, double length, double length2) {
		double x = blockPos.getX() - ReflectionFields.getRenderPosX();
		double y = blockPos.getY() - ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		GL11.glPushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.25);
		drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0F, 0F, 0F, 0F);
		GL11.glColor4d(0, 0, 0, 0.5);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
		GL11.glLineWidth(2.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	public static void blockEspItemFrame(Entity entity, Color c) {
		GL11.glPushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.15F);
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		drawColorBox(
				new AxisAlignedBB(
						entity.getEntityBoundingBox().minX - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
						entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
						entity.getEntityBoundingBox().minZ - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ()),
						entity.getEntityBoundingBox().maxX - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
						entity.getEntityBoundingBox().maxY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
						entity.getEntityBoundingBox().maxZ - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ())),
				0F, 0F, 0F, 0F);
		GL11.glColor4d(0, 0, 0, 0.5);
		drawSelectionBoundingBox(new AxisAlignedBB(
				entity.getEntityBoundingBox().minX - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
				entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
				entity.getEntityBoundingBox().minZ - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ()),
				entity.getEntityBoundingBox().maxX - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
				entity.getEntityBoundingBox().maxY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
				entity.getEntityBoundingBox().maxZ - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ())));
		GL11.glLineWidth(2.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	public static void blockEspBox(BlockPos blockPos, double red, double green, double blue) {
		double x = blockPos.getX() - ReflectionFields.getRenderPosX();
		double y = blockPos.getY() - ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(red, green, blue, 0.15F);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0F, 0F, 0F, 0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	public static void emptyBlockESPBox(BlockPos blockPos) {
		double x = blockPos.getX() - ReflectionFields.getRenderPosX();
		double y = blockPos.getY() - ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(0, 0, 0, 0.5F);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	public static int enemy = 0;
	public static int friend = 1;
	public static int other = 2;
	public static int target = 3;
	public static int team = 4;

	public static void entityESPBox(Entity entity, Color c) {
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		drawSelectionBoundingBox(new AxisAlignedBB(
				entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
				entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
				entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ()),
				entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
				entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
				entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ())));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	public static void nukerBox(BlockPos blockPos, float damage) {
		double x = blockPos.getX() - ReflectionFields.getRenderPosX();
		double y = blockPos.getY() - ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(damage, 1 - damage, 0, 0.15F);
		drawColorBox(
				new AxisAlignedBB(x + 0.5 - damage / 2, y + 0.5 - damage / 2, z + 0.5 - damage / 2,
						x + 0.5 + damage / 2, y + 0.5 + damage / 2, z + 0.5 + damage / 2),
				damage, 1 - damage, 0, 0.15F);
		GL11.glColor4d(0, 0, 0, 0.5F);
		drawSelectionBoundingBox(new AxisAlignedBB(x + 0.5 - damage / 2, y + 0.5 - damage / 2, z + 0.5 - damage / 2,
				x + 0.5 + damage / 2, y + 0.5 + damage / 2, z + 0.5 + damage / 2));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	public static void searchBox(BlockPos blockPos) {
		double x = blockPos.getX() - ReflectionFields.getRenderPosX();
		double y = blockPos.getY() - ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		float sinus = 1F - MathHelper
				.abs(MathHelper.sin(Minecraft.getSystemTime() % 10000L / 10000.0F * (float) Math.PI * 4.0F) * 1F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1F - sinus, sinus, 0F, 0.15F);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1F - sinus, sinus, 0F, 0.15F);
		GL11.glColor4d(0, 0, 0, 0.5);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}

	public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
		Tessellator ts = Tessellator.getInstance();
		BufferBuilder vb = ts.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts X.
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		ts.draw();// Ends X.
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Y.
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		ts.draw();// Ends Y.
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Z.
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
		ts.draw();// Ends Z.
	}
	
	
public static void drawColorBox(AxisAlignedBB axisalignedbb, Color c) {
		
		Tessellator ts = Tessellator.getInstance();
		BufferBuilder vb = ts.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts X.
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		ts.draw();// Ends X.
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Y.
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		ts.draw();// Ends Y.
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Z.
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		ts.draw();// Ends Z.
	}

	public static void drawTrajectoriesColorBox(AxisAlignedBB axisalignedbb, Color c) {
	
	Tessellator ts = Tessellator.getInstance();
	BufferBuilder vb = ts.getBuffer();
	

	vb.begin(7, DefaultVertexFormats.POSITION_TEX);
	vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
	ts.draw();// Ends Y.
	
}

	public static void tracerLine(int x, int y, int z, Color color) {
		x += - ReflectionFields.getRenderPosX();
		y += - ReflectionFields.getRenderPosY();
		z += - ReflectionFields.getRenderPosZ();
		GL11.glPushMatrix();
		GL11.glEnable(2848);
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glLineWidth(1.5F);
		GL11.glColor3f(0.0F, 230.0F, 255.0F);
		Vec3d eyes = new Vec3d(0.0D, 0.0D, 1.0D).rotatePitch(-(float) Math.toRadians(Wrapper.mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(Wrapper.mc.player.rotationYaw));
		GL11.glBegin(2);

		GL11.glVertex3d(eyes.x, Wrapper.mc.player.getEyeHeight() + eyes.y, eyes.z);
		GL11.glVertex3d(x, y, z);

		GL11.glEnd();
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		tessellator.draw();
	}
	
	public static void drawSelectionBoundingBoxColor(AxisAlignedBB boundingBox, Color c) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		tessellator.draw();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		tessellator.draw();
		vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		tessellator.draw();
	}

	public static void scissorBox(int x, int y, int xend, int yend) {
		int width = xend - x;
		int height = yend - y;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int factor = sr.getScaleFactor();
		int bottomY = Minecraft.getMinecraft().currentScreen.height - yend;
		glScissor(x * factor, bottomY * factor, width * factor, height * factor);
	}

	public static void setColor(Color c) {
		glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}

	public static float[] getColorValues(Color c) {
		return new float[] { c.getRed() / 255f, c.getGreen() / 255f, c.getGreen() / 255f };
	}

	private static Minecraft mc = Wrapper.mc;

	/*
	 * public static void setWindowCursor(int cursor) {
	 * //Adolf.getMinecraft().mcCanvas.setCursor(Cursor.getPredefinedCursor(cursor))
	 * ; }
	 */

	public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
		final Sphere s = new Sphere();
		GL11.glPushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.2F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		s.setDrawStyle(GLU.GLU_SILHOUETTE);
		GL11.glTranslated(x - ReflectionFields.getRenderPosX(), y - ReflectionFields.getRenderPosY(), z - ReflectionFields.getRenderPosZ());
		s.draw(size, slices, stacks);
		GL11.glLineWidth(2.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVLine(x, y + 1, y1 - 2, borderC);
		drawVLine(x1 - 1, y + 1, y1 - 2, borderC);
		drawHLine(x + 2, x1 - 3, y, borderC);
		drawHLine(x + 2, x1 - 3, y1 - 1, borderC);
		drawHLine(x + 1, x + 1, y + 1, borderC);
		drawHLine(x1 - 2, x1 - 2, y + 1, borderC);
		drawHLine(x1 - 2, x1 - 2, y1 - 2, borderC);
		drawHLine(x + 1, x + 1, y1 - 2, borderC);
		drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
	}

	public static void drawBetterBorderedRect(int x, float y, int x1, int y1, float size, int borderC, int insideC) {
		drawRect(x, y, x1, y1, insideC); // inside
		drawRect(x, y, x1, y + size, borderC); // top
		drawRect(x, y1, x1, y1 + size, borderC); // bottom
		drawRect(x1, y, x1 + size, y1 + size, borderC); // left
		drawRect(x, y, x + size, y1 + size, borderC); // right
	}

	public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
		drawRect((float) x, (float) y, (float) x2, (float) y2, col2);

		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public static void drawHLine(float par1, float par2, float par3, int par4) {
		if (par2 < par1) {
			float var5 = par1;
			par1 = par2;
			par2 = var5;
		}

		drawRect(par1, par3, par2 + 1, par3 + 1, par4);
	}

	public static void drawVLine(float par1, float par2, float par3, int par4) {
		if (par3 < par2) {
			float var5 = par2;
			par2 = par3;
			par3 = var5;
		}

		drawRect(par1, par2 + 1, par1 + 1, par3, par4);
	}

	public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd,
			int paramColor) {
		float alpha = (float) (paramColor >> 24 & 0xFF) / 255F;
		float red = (float) (paramColor >> 16 & 0xFF) / 255F;
		float green = (float) (paramColor >> 8 & 0xFF) / 255F;
		float blue = (float) (paramColor & 0xFF) / 255F;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;

		float f4 = (float) (col2 >> 24 & 0xFF) / 255F;
		float f5 = (float) (col2 >> 16 & 0xFF) / 255F;
		float f6 = (float) (col2 >> 8 & 0xFF) / 255F;
		float f7 = (float) (col2 & 0xFF) / 255F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2,
			int col3) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(1F);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		drawGradientRect(x, y, x2, y2, col2, col3);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
		GL11.glPushMatrix();
		float f1 = (float) (color >> 24 & 255) / 255.0F;
		float f2 = (float) (color >> 16 & 255) / 255.0F;
		float f3 = (float) (color >> 8 & 255) / 255.0F;
		float f4 = (float) (color & 255) / 255.0F;
		GL11.glTranslatef(x, y, 0);
		GL11.glColor4f(f2, f3, f4, f1);
		GL11.glLineWidth(width);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		if (angle > 0) {
			GL11.glBegin(GL11.GL_LINE_STRIP);

			for (int i = 0; i < angle; i++) {
				float a = (float) (i * (angle * Math.PI / points));
				float xc = (float) (Math.cos(a) * radius);
				float yc = (float) (Math.sin(a) * radius);
				GL11.glVertex2f(xc, yc);
			}

			GL11.glEnd();
		}

		if (angle < 0) {
			GL11.glBegin(GL11.GL_LINE_STRIP);

			for (int i = 0; i > angle; i--) {
				float a = (float) (i * (angle * Math.PI / points));
				float xc = (float) (Math.cos(a) * -radius);
				float yc = (float) (Math.sin(a) * -radius);
				GL11.glVertex2f(xc, yc);
			}

			GL11.glEnd();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL13.GL_MULTISAMPLE);
		GL11.glDisable(GL11.GL_MAP1_VERTEX_3);
		GL11.glPopMatrix();
	}

	public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		r *= 2;
		cx *= 2;
		cy *= 2;
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		float theta = (float) (2 * 3.1415926 / (num_segments));
		float p = (float) Math.cos(theta);// calculate the sine and cosine
		float s = (float) Math.sin(theta);
		float t;
		GL11.glColor4f(f1, f2, f3, f);
		float x = r;
		float y = 0;// start at angle = 0
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(770, 771);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int ii = 0; ii < num_segments; ii++) {
			GL11.glVertex2f(x + cx, y + cy);// final vertex vertex

			// rotate the stuff
			t = x;
			x = p * x - s * y;
			y = s * t + p * y;
		}
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glScalef(2F, 2F, 2F);
	}

	public static void drawFullCircle(int cx, int cy, double r, int c) {
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		r *= 2;
		cx *= 2;
		cy *= 2;
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for (int i = 0; i <= 360; i++) {
			double x = Math.sin((i * Math.PI / 180)) * r;
			double y = Math.cos((i * Math.PI / 180)) * r;
			GL11.glVertex2d(cx + x, cy + y);
		}
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glScalef(2F, 2F, 2F);
	}

	public static void drawOutlinedBoundingBox(net.minecraft.util.math.AxisAlignedBB par1AxisAlignedBB) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	public static void drawBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		vertexbuffer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	public static void drawESP(double d, double d1, double d2, double r, double b, double g) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(1.0F);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glColor4d(r, g, b, 0.1825F);
		drawColorBox(new AxisAlignedBB(d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0), 0F, 0F, 0F, 0F);
		GL11.glColor4d(0, 0, 0, 0.5);
		drawSelectionBoundingBox(new AxisAlignedBB(d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0));
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void drawChestESP(double d, double d1, double d2, double r, double b, double g, double length,
			double length2) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(1.0F);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glColor4d(r, g, b, 0.15);
		drawColorBox(new AxisAlignedBB(d, d1, d2, d + length2, d1 + 1.0, d2 + length), 0F, 0F, 0F, 0F);
		GL11.glColor4d(0, 0, 0, 0.5);
		drawSelectionBoundingBox(new AxisAlignedBB(d, d1, d2, d + length2, d1 + 1.0, d2 + length));
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z,
			int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-0.025F, -0.025F, 0.025F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		int i = fontRendererIn.getStringWidth(str) / 2;
		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos((double) (-i - 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F)
				.endVertex();
		vertexbuffer.pos((double) (-i - 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F)
				.endVertex();
		vertexbuffer.pos((double) (i + 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F)
				.endVertex();
		vertexbuffer.pos((double) (i + 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F)
				.endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();

		GlStateManager.depthMask(true);
		fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, -1);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	public static void drawEntityESP(Entity entity, Color c) {
		GL11.glPushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.15F);
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		drawColorBox(
				new AxisAlignedBB(
						entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
						entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
						entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ()),
						entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
						entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
						entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ())),
				0F, 0F, 0F, 0F);
		GL11.glColor4d(0, 0, 0, 0.5);
		drawSelectionBoundingBox(new AxisAlignedBB(
				entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
				entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
				entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ()),
				entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - ReflectionFields.getRenderPosX()),
				entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + (entity.posY - ReflectionFields.getRenderPosY()),
				entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - ReflectionFields.getRenderPosZ())));
		GL11.glLineWidth(2.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	public static void drawPlayerESP(double d, double d1, double d2, EntityPlayer ep, double e, double f) {
		if (!(ep instanceof EntityPlayerSP)) {
			GL11.glPushMatrix();
			GL11.glEnable(3042);
			GL11.glColor4f(0.7F, 0.0F, 0.0F, 0.15F);
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glLineWidth(1.0F);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			drawBoundingBox(new AxisAlignedBB(d - f, d1 + 0.1, d2 - f, d + f, d1 + e + 0.25, d2 + f));
			GL11.glColor4f(0.7F, 0.0F, 0.0F, 1F);
			drawOutlinedBoundingBox(new AxisAlignedBB(d - f, d1 + 0.1, d2 - f, d + f, d1 + e + 0.25, d2 + f));
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(3042);
			GL11.glPopMatrix();
		}
	}

	public static void blockEspBoxplayer(BlockPos blockPos, double red, double green, double blue) {
		
		double x = blockPos.getX() - ReflectionFields.getRenderPosX();
		double y = blockPos.getY() - ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(red, green, blue, 0.15F);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 2.0, z + 1.0), 0F, 0F, 0F, 0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	
	
	
	public static void blockESPOutline(BlockPos blockPos, Color c, double length, double length2) {
		double x = ReflectionFields.getRenderPosX();
		double y = ReflectionFields.getRenderPosY();
		double z = blockPos.getZ() - ReflectionFields.getRenderPosZ();
		checkSetupFBO();
	    GL11.glPushAttrib(1048575);
	    GL11.glDisable(3008);
	    GL11.glDisable(3553);
	    GL11.glDisable(2896);
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    GL11.glLineWidth(3);
	    GL11.glEnable(2848);
	    GL11.glEnable(2960);
	    GL11.glClear(1024);
	    GL11.glClearStencil(15);
	    GL11.glStencilFunc(512, 1, 15);
	    GL11.glStencilOp(7681, 7681, 7681);
	    GL11.glPolygonMode(1032, 6913);
	    GL11.glStencilFunc(512, 0, 15);
	    GL11.glStencilOp(7681, 7681, 7681);
	    GL11.glPolygonMode(1032, 6914);
	    GL11.glStencilFunc(514, 1, 15);
	    GL11.glStencilOp(7680, 7680, 7680);
	    GL11.glPolygonMode(1032, 6913);
	    GL11.glPolygonOffset(1.0F, 2000000.0F);
	    GL11.glDisable(10754);
	    GL11.glEnable(2929);
	    GL11.glDepthMask(true);
	    GL11.glDisable(2960);
	    GL11.glDisable(2848);
	    GL11.glHint(3154, 4352);
	    GL11.glEnable(3042);
	    GL11.glEnable(2896);
	    GL11.glEnable(3553);
	    GL11.glEnable(3008);
	    GL11.glPopAttrib();
	}
	
	
	public static void renderBeamSegment(double x, double y, double z, double partialTicks, double textureScale, double totalWorldTime, int yOffset, int height, float[] colors)
    {
        renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, yOffset, height, colors, 0.2D, 0.25D);
    }

    public static void renderBeamSegment(double x, double y, double z, double partialTicks, double textureScale, double totalWorldTime, int yOffset, int height, float[] colors, double beamRadius, double glowRadius)
    {
        int i = yOffset + height;
        GlStateManager.glTexParameteri(3553, 10242, 10497);
        GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double d0 = totalWorldTime + partialTicks;
        double d1 = height < 0 ? d0 : -d0;
        double d2 = MathHelper.frac(d1 * 0.2D - (double)MathHelper.floor(d1 * 0.1D));
        float f = colors[0];
        float f1 = colors[1];
        float f2 = colors[2];
        double d3 = d0 * 0.025D * -1.5D;
        double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * beamRadius;
        double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * beamRadius;
        double d6 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * beamRadius;
        double d7 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * beamRadius;
        double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * beamRadius;
        double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * beamRadius;
        double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * beamRadius;
        double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * beamRadius;
        double d12 = 0.0D;
        double d13 = 1.0D;
        double d14 = -1.0D + d2;
        double d15 = (double)height * textureScale * (0.5D / beamRadius) + d14;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x + d4, y + (double)i, z + d5).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d4, y + (double)yOffset, z + d5).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d6, y + (double)yOffset, z + d7).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d6, y + (double)i, z + d7).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d10, y + (double)i, z + d11).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d10, y + (double)yOffset, z + d11).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d8, y + (double)yOffset, z + d9).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d8, y + (double)i, z + d9).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d6, y + (double)i, z + d7).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d6, y + (double)yOffset, z + d7).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d10, y + (double)yOffset, z + d11).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d10, y + (double)i, z + d11).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d8, y + (double)i, z + d9).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d8, y + (double)yOffset, z + d9).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d4, y + (double)yOffset, z + d5).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        bufferbuilder.pos(x + d4, y + (double)i, z + d5).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);
        d3 = 0.5D - glowRadius;
        d4 = 0.5D - glowRadius;
        d5 = 0.5D + glowRadius;
        d6 = 0.5D - glowRadius;
        d7 = 0.5D - glowRadius;
        d8 = 0.5D + glowRadius;
        d9 = 0.5D + glowRadius;
        d10 = 0.5D + glowRadius;
        d11 = 0.0D;
        d12 = 1.0D;
        d13 = -1.0D + d2;
        d14 = (double)height * textureScale + d13;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x + d3, y + (double)i, z + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d3, y + (double)yOffset, z + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d5, y + (double)yOffset, z + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d5, y + (double)i, z + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d9, y + (double)i, z + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d9, y + (double)yOffset, z + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d7, y + (double)yOffset, z + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d7, y + (double)i, z + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d5, y + (double)i, z + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d5, y + (double)yOffset, z + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d9, y + (double)yOffset, z + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d9, y + (double)i, z + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d7, y + (double)i, z + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d7, y + (double)yOffset, z + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d3, y + (double)yOffset, z + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        bufferbuilder.pos(x + d3, y + (double)i, z + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
    }
    
    
    public static void drawWayPointTracer(BlockPos blockPos, float red, float green, float blue)
	{
		try
		{
			GL11.glPushMatrix();
			GL11.glEnable(2848);
			GL11.glDisable(2929);
			GL11.glDisable(3553);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(770, 771);
			GL11.glEnable(3042);
	        GL11.glLineWidth(1.5F);
			GL11.glColor3f(red, green, blue);
			Vec3d eyes = new Vec3d(0.0D, 0.0D, 1.0D)
					.rotatePitch(-(float) Math.toRadians(Wrapper.mc.player.rotationPitch))
					.rotateYaw(-(float) Math.toRadians(Wrapper.mc.player.rotationYaw));

			GL11.glBegin(2);

			GL11.glVertex3d(eyes.x, Wrapper.mc.player.getEyeHeight() + eyes.y, eyes.z);
			GL11.glVertex3d(blockPos.getX() + 0.5 - ReflectionFields.getRenderPosX(), blockPos.getY() - ReflectionFields.getRenderPosY(), blockPos.getZ() - ReflectionFields.getRenderPosZ() + 0.5);
			GL11.glEnd();
			
			GL11.glDisable(3042);
			GL11.glDepthMask(true);
			GL11.glEnable(3553);
			GL11.glEnable(2929);
			GL11.glDisable(2848);
			GL11.glPopMatrix();
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}catch(Exception e) {}
	}

    public static void circleESP(BlockPos blockPos, double radius, Color color) {
        double x = blockPos.getX() + 0.5 - ReflectionFields.getRenderPosX();
        double y = blockPos.getY() - ReflectionFields.getRenderPosY();
        double z = blockPos.getZ() + 0.5 - ReflectionFields.getRenderPosZ();
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.25);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex3d(x + Math.sin(i * 3.141592653589793 / 180.0) * radius, y, z + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glLineWidth(2.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }
	
	
	public static void checkSetupFBO()
	  {
	    Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
	    if (fbo != null) {
	      if (fbo.depthBuffer > -1)
	      {
	        setupFBO(fbo);
	        
	        fbo.depthBuffer = -1;
	      }
	    }
	  }
	  
	  public static void setupFBO(Framebuffer fbo)
	  {
	    EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
	    
	    int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
	    
	    EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
	    
	    EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	    
	    EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
	    
	    EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	  }
	
	    public static void drawOutlinedBox() {
	        drawOutlinedBox(DEFAULT_AABB);
	    }

	    private static final AxisAlignedBB DEFAULT_AABB =
	            new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	    
	    public static void drawOutlinedBox(AxisAlignedBB bb) {
	        glBegin(GL_LINES);
	        {
	            glVertex3d(bb.minX, bb.minY, bb.minZ);
	            glVertex3d(bb.maxX, bb.minY, bb.minZ);

	            glVertex3d(bb.maxX, bb.minY, bb.minZ);
	            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

	            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
	            glVertex3d(bb.minX, bb.minY, bb.maxZ);

	            glVertex3d(bb.minX, bb.minY, bb.maxZ);
	            glVertex3d(bb.minX, bb.minY, bb.minZ);

	            glVertex3d(bb.minX, bb.minY, bb.minZ);
	            glVertex3d(bb.minX, bb.maxY, bb.minZ);

	            glVertex3d(bb.maxX, bb.minY, bb.minZ);
	            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

	            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
	            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

	            glVertex3d(bb.minX, bb.minY, bb.maxZ);
	            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

	            glVertex3d(bb.minX, bb.maxY, bb.minZ);
	            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

	            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
	            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

	            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
	            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

	            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
	            glVertex3d(bb.minX, bb.maxY, bb.minZ);
	        }
	        glEnd();
	    }


		public static void drawSolidBox(AxisAlignedBB bb)
		{
			glBegin(GL_QUADS);
			{
				glVertex3d(bb.minX, bb.minY, bb.minZ);
				glVertex3d(bb.maxX, bb.minY, bb.minZ);
				glVertex3d(bb.maxX, bb.minY, bb.maxZ);
				glVertex3d(bb.minX, bb.minY, bb.maxZ);
				
				glVertex3d(bb.minX, bb.maxY, bb.minZ);
				glVertex3d(bb.minX, bb.maxY, bb.maxZ);
				glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
				glVertex3d(bb.maxX, bb.maxY, bb.minZ);
				
				glVertex3d(bb.minX, bb.minY, bb.minZ);
				glVertex3d(bb.minX, bb.maxY, bb.minZ);
				glVertex3d(bb.maxX, bb.maxY, bb.minZ);
				glVertex3d(bb.maxX, bb.minY, bb.minZ);
				
				glVertex3d(bb.maxX, bb.minY, bb.minZ);
				glVertex3d(bb.maxX, bb.maxY, bb.minZ);
				glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
				glVertex3d(bb.maxX, bb.minY, bb.maxZ);
				
				glVertex3d(bb.minX, bb.minY, bb.maxZ);
				glVertex3d(bb.maxX, bb.minY, bb.maxZ);
				glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
				glVertex3d(bb.minX, bb.maxY, bb.maxZ);
				
				glVertex3d(bb.minX, bb.minY, bb.minZ);
				glVertex3d(bb.minX, bb.minY, bb.maxZ);
				glVertex3d(bb.minX, bb.maxY, bb.maxZ);
				glVertex3d(bb.minX, bb.maxY, bb.minZ);
			}
			glEnd();
		}
	
}