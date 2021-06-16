package com.ragemachine.moon;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class    RPC {

    //thanks to Xulu although you can't exactly skid RPC since its kind of the same thing everywhere but thanks for the framework and shit
    private boolean running = true;
    private long created = 0;
    public void start() {
        Minecraft mc = Minecraft.getMinecraft();
        ServerData svr;
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "756565031906181151";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ez");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();

        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Gaming";
        presence.state = "synapse";
        presence.largeImageKey = "blue";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try{
                    presence.largeImageKey = "blue";
                    presence.largeImageText = "Synapse Beta v." + MoonHack.MODVER;
                    if (mc.isIntegratedServerRunning()) {
                        presence.details = "Singleplayer";
                        presence.state = "Gaming";
                    }
                    else if (mc.getCurrentServerData() != null) {
                        if (!mc.getCurrentServerData().serverIP.equals(presence.state)) {
                            presence.details = "Playing On: " + mc.getCurrentServerData().serverIP;
                            presence.state = "Thanks myrrh and RAGE MACHINE for Synapse!";
                        }
                    }
                    else {
                        presence.details = "Menu";
                        presence.state = "AFK";
                    }
                    lib.Discord_UpdatePresence(presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }
    public void shutdown() {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        lib.Discord_Shutdown();
    }
}
