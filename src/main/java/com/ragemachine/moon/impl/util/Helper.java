

package com.ragemachine.moon.impl.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public interface Helper
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final World world = Minecraft.getMinecraft().world;
    public static final EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
    public static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static final EventBus EVENT_BUS = MinecraftForge.EVENT_BUS;


}
