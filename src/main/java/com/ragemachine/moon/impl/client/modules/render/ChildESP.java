package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.VectorUtils;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.io.InputStream;
import javax.imageio.ImageIO;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class ChildESP extends Module {

    private final Value<Boolean> noRenderPlayers = addSetting(new Value("No Render Players", false));
    private final Value<Image> image = addSetting(new Value("Image", Image.CuteLoli1));

    private ResourceLocation loli1 = new ResourceLocation("images/loli1.png");
    private ResourceLocation loli2 = new ResourceLocation("images/loli2.png");
    private ResourceLocation loli3 = new ResourceLocation("images/loli3.png");
    private ResourceLocation loli4 = new ResourceLocation("images/loli4.png");

    public ChildESP() {
        super("ChildESP","overlay cute images over players", Category.RENDER, true, false, true);
        onLoad();
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private <T> BufferedImage getImage(final T source, final ThrowingFunction<T, BufferedImage> readFunction) {
        try {
            return readFunction.apply(source);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean shouldDraw(final EntityLivingBase entity) {

        return !entity.equals(mc.player) && entity.getHealth() > 0f && EntityUtil.isPlayer(entity);
    }

    private ICamera camera = new Frustum();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (this.loli1 == null) {
            return;
        }
        if (this.loli2 == null) {
            return;
        }
        if (this.loli3 == null) {
            return;
        }
        if (this.loli4 == null) {
            return;
        }
        double d3 = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)event.getPartialTicks();
        double d4 = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)event.getPartialTicks();
        double d5 = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)event.getPartialTicks();

        camera.setPosition(d3,  d4,  d5);
        final List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
        players.sort(Comparator.comparing(entityPlayer -> mc.player.getDistance((EntityPlayer)entityPlayer)).reversed());
        for (final EntityPlayer player : players) {
            if (player != mc.getRenderViewEntity() && player.isEntityAlive() && camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) {
                final EntityLivingBase living = (EntityLivingBase) player;
                final Vec3d bottomVec = EntityUtil.getInterpolatedPos((Entity) living, event.getPartialTicks());
                final Vec3d topVec = bottomVec.add(new Vec3d(0.0, player.getRenderBoundingBox().maxY - player.posY, 0.0));
                final VectorUtils.ScreenPos top = VectorUtils._toScreen(topVec.x, topVec.y, topVec.z);
                final VectorUtils.ScreenPos bot = VectorUtils._toScreen(bottomVec.x, bottomVec.y, bottomVec.z);
                if (!top.isVisible && !bot.isVisible) {
                    continue;
                }
                final int width;
                final int height = width = bot.y - top.y;
                final int x = (int) (top.x - width / 1.8);
                final int y = top.y;

                if (image.getValue() == Image.CuteLoli1) {
                    mc.renderEngine.bindTexture(this.loli1);
                    GlStateManager.color(255.0f, 255.0f, 255.0f);
                    Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float) width, (float) height);
                }
                if (image.getValue() == Image.CuteLoli2) {
                    mc.renderEngine.bindTexture(this.loli2);
                    GlStateManager.color(255.0f, 255.0f, 255.0f);
                    Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float) width, (float) height);
                }
                if (image.getValue() == Image.CuteLoli3) {
                    mc.renderEngine.bindTexture(this.loli3);
                    GlStateManager.color(255.0f, 255.0f, 255.0f);
                    Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float) width, (float) height);
                }
                if (image.getValue() == Image.CuteLoli4) {
                    mc.renderEngine.bindTexture(this.loli4);
                    GlStateManager.color(255.0f, 255.0f, 255.0f);
                    Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float) width, (float) height);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Pre event) {
        if (this.noRenderPlayers.getValue() && !event.getEntity().equals(mc.player)) {
            event.setCanceled(true);
        }
    }

    public void onLoad() {
        BufferedImage image = null;
        DynamicTexture dynamicTexture;
        try {
            if (getFile(this.image.getValue().getName()) != null) {
                image = this.getImage(getFile(this.image.getValue().getName()), ImageIO::read);
            }
            /*
            else {
                image = this.getImage(new URL(url.getUrl()), ImageIO::read);
                if (image != null) {
                    try {
                        ImageIO.write(image, "png", getCache(url));
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            */
            else {
                dynamicTexture = new DynamicTexture(image);
                dynamicTexture.loadTexture(mc.getResourceManager());
                this.loli1 = mc.getTextureManager().getDynamicTextureLocation("SYNAPSE_" + this.image.getValue().name(), dynamicTexture);
                this.loli2 = mc.getTextureManager().getDynamicTextureLocation("SYNAPSE_" + this.image.getValue().name(), dynamicTexture);
                this.loli3 = mc.getTextureManager().getDynamicTextureLocation("SYNAPSE_" + this.image.getValue().name(), dynamicTexture);
                this.loli4 = mc.getTextureManager().getDynamicTextureLocation("SYNAPSE_" + this.image.getValue().name(), dynamicTexture);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R>
    {
        R apply(final T p0) throws IOException;
    }
    
    private InputStream getFile(String string) {
        return ChildESP.class.getResourceAsStream(string);
    }

    private enum Image {
        CuteLoli1("/images/loli1.png"),
        CuteLoli2("/images/loli2.png"),
        CuteLoli3("/images/loli3.png"),
        CuteLoli4("/images/loli4.png");
        String name;

        Image(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
