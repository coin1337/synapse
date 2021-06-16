package com.ragemachine.moon.impl.manager;

import com.google.common.base.Strings;
import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.*;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.client.ClientControl;
import com.ragemachine.moon.impl.util.TextUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.Objects;
import java.util.UUID;

public class MoonEventManager extends Hack {

    private final Timer timer = new Timer();
    private final Timer logoutTimer = new Timer();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onUnload() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!fullNullCheck() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals(mc.player)) {
            MoonHack.getTotemManager.onUpdate();
            MoonHack.getInventoryManager.update();
            MoonHack.HOLE_MANAGER.update();
            MoonHack.getModuleManager.onUpdate();
            MoonHack.getModuleManager.sortModules(true);
            timer.reset();
        }
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        logoutTimer.reset();
        MoonHack.getModuleManager.onLogin();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        MoonHack.getModuleManager.onLogout();
        MoonHack.POTION_MANAGER.onLogout();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(fullNullCheck()) {
            return;
        }

        MoonHack.getModuleManager.onTick();

        for (EntityPlayer player : mc.world.playerEntities) {

            if (player == null || player.getHealth() > 0) {
                continue;
            }

            MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
            MoonHack.getTotemManager.onDeath(player);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if(fullNullCheck()) return;

        if(event.getStage() == 0) {
            MoonHack.getSpeedManager.updateValues();
            MoonHack.getRotationManager.updateRotations();
            MoonHack.getPositionManager.updatePosition();
        }

        if(event.getStage() == 1) {
            MoonHack.getRotationManager.restoreRotations();
            MoonHack.getPositionManager.restorePosition();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if(event.getStage() != 0) {
            return;
        }

        MoonHack.getServerManager.onPacketReceived();

        if(event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if(packet.getOpCode() == 35) {
                if (packet.getEntity(mc.world) instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer)packet.getEntity(mc.world);
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player));
                    MoonHack.getTotemManager.onTotemPop(player);
                    MoonHack.POTION_MANAGER.onTotemPop(player);
                }
            }
        }

        if(event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && logoutTimer.passedS(1)) {
            final SPacketPlayerListItem packet = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction())) {
                return;
            }

            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null)
                    .forEach(data -> {
                                final UUID id = data.getProfile().getId();
                                switch(packet.getAction()) {
                                    case ADD_PLAYER:
                                        String name = data.getProfile().getName();
                                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(0, id, name));
                                        break;
                                    case REMOVE_PLAYER:
                                        EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                                        if(entity != null) {
                                            String logoutName = entity.getName();
                                            MinecraftForge.EVENT_BUS.post(new ConnectionEvent(1, entity, id, logoutName));
                                        } else {
                                            MinecraftForge.EVENT_BUS.post(new ConnectionEvent(2, id, null));
                                        }
                                        break;
                                }
                            });
        }

        if(event.getPacket() instanceof SPacketTimeUpdate) {
            MoonHack.getServerManager.update();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }

        mc.profiler.startSection("moonhack");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        MoonHack.getModuleManager.onRender3D(render3dEvent);
        //MinecraftForge.EVENT_BUS.post(render3dEvent);
        GlStateManager.glLineWidth(1f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        mc.profiler.endSection();
    }
    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            MoonHack.TEXT_MANAGER.updateResolution();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            final ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            MoonHack.getModuleManager.onRender2D(render2DEvent);
            GlStateManager.color(1.f, 1.f, 1.f, 1.f);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            MoonHack.getModuleManager.onKeyPressed(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if(event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    MoonHack.getCommandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage(TextUtil.RED +  "An error occurred while running this command. Check the log!");
            }
            event.setMessage("");
        }
    }

    public void onEntityRemoved(Entity entityIn) {
        MinecraftForge.EVENT_BUS.post(new EntityRemovedEvent(entityIn));
    }
}
