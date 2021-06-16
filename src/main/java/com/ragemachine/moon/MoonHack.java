package com.ragemachine.moon;

import com.ragemachine.moon.impl.client.gui.font.CFont;
import com.ragemachine.moon.impl.client.modules.client.Colors;
import com.ragemachine.moon.impl.manager.*;
import com.ragemachine.moon.impl.util.GLSLSandboxShader;
import com.ragemachine.moon.impl.util.Wrapper;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;

@Mod(modid = MoonHack.MODID, name = MoonHack.MODNAME, version = MoonHack.MODVER)
public class MoonHack {

	public static final String MODID = "moonhack";
	public static final String MODNAME = "moonhack";
	public static final String MODVER = "0.5.7.2";
	public static final String NAME_UNICODE = "\u06DE\u1D0D\u1D0F\u1D0F\u0274 \u029C\u1D00\u1D04\u1D0B \u06DE"; //"\u1D18\u029C\u1D0F\u0299\u1D0F\uA731.\u1D07\u1D1C";
	public static final String MOONHACK_UNICODE = "\u06DE\u1D0D\u1D0F\u1D0F\u0274 \u029C\u1D00\u1D04\u1D0B \u06DE";
	public static final String MOONHACK_DISCORD = "discord.gg/8bscatk, MOONHACK ONTOP!";
	public static final String MOONHACK_DSICORD_FANCY = "\u1D05\u026As\u1D04\u1D0F\u0280\u1D05.\u0262\u0262/\uD835\uDFFE\u0299s\u1D04\u1D00\u1D1B\u1D0B , \u1D0D\u1D0F\u1D0F\u0274 \u029C\u1D00\u1D04\u1D0B \u1D0F\u0274\u1D1B\u1D0F\u1D18!";
	public static final String CHAT_SUFFIX = " \u23D0 " + NAME_UNICODE;
	public static final String MOONHACK_SUFFIX = " \u23D0 " + MOONHACK_UNICODE;
	public static final Logger LOGGER = LogManager.getLogger("moonhack");

	public static ModuleManager getModuleManager;
	public static SpeedManager getSpeedManager;
	public static long initTime;
	private RPC discordRpc = new RPC();
	public static PositionManager getPositionManager;
	public static GLSLSandboxShader getBackgroundShader;
	public static RotationManager getRotationManager;
	public static CommandManager getCommandManager;
	public static MoonEventManager getEventManager;
	public static ConfigManager CONFIG_MANAGER;
	public static CFont cFont;
	public static FileManager getFileManager;
	public static FriendManager getFriendManager;
	public static TextManager TEXT_MANAGER;
	public static ColorManager COLOR_MANAGER;
	public static ServerManager getServerManager;
	public static PotionManager POTION_MANAGER;
	public static InventoryManager getInventoryManager;
	public static PacketManager getPacketManager;
	public static ReloadManager getReloadManager;
	public static Colors getColors;

	public static TotemPopManager getTotemManager;
	public static HoleManager HOLE_MANAGER;
	public static RenderManager RENDER_MANAGER;
	public static final EventBus EVENT_BUS = new EventManager();
	private static boolean unloaded = false;
	public static int rgb;

	@Mod.Instance
	public static MoonHack INSTANCE;


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER.info("Backdoori- i mean loading..");
		LOGGER.info("Still loading...");
		LOGGER.info("Just a lil bit longer...");
		LOGGER.info("done! Synapse: ON!");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		Display.setTitle("Synapse - v." + MODVER);
		discordRpc.start();
		load();
	}

	public static void load() {
		LOGGER.info("\n\nLoading Synapse " + MODVER);
		unloaded = false;
		if(getReloadManager != null) {
			getReloadManager.unload();
			getReloadManager = null;
		}

		getTotemManager = new TotemPopManager();
		getPacketManager = new PacketManager();
		getServerManager = new ServerManager();
		COLOR_MANAGER = new ColorManager();
		TEXT_MANAGER = new TextManager();
		getModuleManager = new ModuleManager();
		getSpeedManager = new SpeedManager();
		getRotationManager = new RotationManager();
		getPositionManager = new PositionManager();
		getCommandManager = new CommandManager();
		getEventManager = new MoonEventManager();
		CONFIG_MANAGER = new ConfigManager();
		getFileManager = new FileManager();
		getFriendManager = new FriendManager();
		POTION_MANAGER = new PotionManager();
		getInventoryManager = new InventoryManager();
		HOLE_MANAGER = new HoleManager();
		LOGGER.info("Initialized ClientControl");

		getModuleManager.init();
		LOGGER.info("Modules loaded.");
		CONFIG_MANAGER.init();
		getEventManager.init();
		LOGGER.info("MoonEventManager loaded.");
		TEXT_MANAGER.init(true);
		getModuleManager.onLoad();
		LOGGER.info("Synapse initialized!\n");
	}

	@SubscribeEvent
	public void tick(TickEvent e){
	}

	public static Color getColorByRange(Entity entity) {
		if (Wrapper.mc.player.getDistanceSq(entity) <= 32) {
			return new Color(170, 0, 0);
		} else if (Wrapper.mc.player.getDistanceSq(entity) >= 32 && Wrapper.mc.player.getDistanceSq(entity) <= 96) {
			return new Color(255, 255, 85);
		} else {
			return new Color(0, 170, 0);
		}
	}

	public static void unload(boolean unload) {
		LOGGER.info("\n\nUnloading Synapse " + MODVER);
		if(unload) {
			getReloadManager = new ReloadManager();
			getReloadManager.init(getCommandManager != null ? getCommandManager.getPrefix() : ".");
		}
		onUnload();
		getEventManager = null;
		HOLE_MANAGER = null;
		getModuleManager = null;
		getTotemManager = null;
		getServerManager = null;
		COLOR_MANAGER = null;
		RENDER_MANAGER = null;
		TEXT_MANAGER = null;
		getSpeedManager = null;
		getRotationManager = null;
		getPositionManager = null;
		getCommandManager = null;
		CONFIG_MANAGER = null;
		getFileManager = null;
		getFriendManager = null;
		POTION_MANAGER = null;
		getInventoryManager = null;

		LOGGER.info("Synapse unloaded!\n");
		rgb = Color.HSBtoRGB(0.01f, MoonHack.getColors.rainbowSaturation.getValue() / 255f, MoonHack.getColors.rainbowBrightness.getValue() / 255f);

	}

	public static void reload() {
		unload(false);
		load();
	}

	public static void onUnload() {
		if(!unloaded) {
			getEventManager.onUnload();
			getModuleManager.onUnload();
			CONFIG_MANAGER.saveConfig(CONFIG_MANAGER.config.replaceFirst("moonhack/", ""));
			getModuleManager.onUnloadPost();
			unloaded = true;
		}
	}
}
