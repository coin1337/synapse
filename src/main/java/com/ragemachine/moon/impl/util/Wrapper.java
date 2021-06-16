package com.ragemachine.moon.impl.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

// Created by 086 on 11/11/2017.

public class Wrapper{

	private static FontRenderer fontRenderer;

	public static Minecraft mc = Minecraft.getMinecraft();

	public static Minecraft getMinecraft(){
		return Minecraft.getMinecraft();
	}

	public static EntityPlayerSP getPlayer(){
		return getMinecraft().player;
	}

	public static World getWorld() {
		return getMinecraft().world;
	}

	public static int getKey(String keyname){
		return Keyboard.getKeyIndex(keyname.toUpperCase());
	}

	public static FontRenderer getFontRenderer(){
		return fontRenderer;
	}

	public static Block getBlock(BlockPos pos) {
		return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
	}
	public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames)
	{
		try
		{
			findField(classToAccess, fieldNames).set(instance, value);
		}
		catch (Exception e) {}
	}
	private static Field findField(Class<?> clazz, String... fieldNames)
	{
		Exception failed = null;
		for (String fieldName : fieldNames) {
			try {
				Field f = clazz.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			} catch (Exception e) {
				failed = e;
			}
		}
		return null;
	}

	public static EntityPlayerSP getLocalPlayer() {
		return mc.player;
	}

	@Nullable
	public static Entity getRidingEntity() {
		if (getLocalPlayer() != null) {
			return getLocalPlayer().getRidingEntity();
		} else {
			return null;
		}
	}

	@Nullable
	public static NetworkManager getNetworkManager() {
		return FMLClientHandler.instance().getClientToServerNetworkManager();
	}
}
