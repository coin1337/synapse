package com.ragemachine.moon.impl.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 2 lazy to import static
 */
public interface Globals {
  
  Logger LOGGER = LogManager.getLogger("moonhack");
  Minecraft MC = FMLClientHandler.instance().getClient();
}
