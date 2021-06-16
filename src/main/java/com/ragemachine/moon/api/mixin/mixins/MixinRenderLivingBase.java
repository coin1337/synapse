package com.ragemachine.moon.api.mixin.mixins;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.render.Chams;
import com.ragemachine.moon.impl.client.modules.render.ColorChams;
import com.ragemachine.moon.impl.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean renderMarker;
    float red = 0.0f;
    float green = 0.0f;
    float blue = 0.0f;

    @Shadow
    protected abstract boolean isVisible(EntityLivingBase var1);

    @Shadow
    protected abstract float getSwingProgress(T var1, float var2);

    @Shadow
    protected abstract float interpolateRotation(float var1, float var2, float var3);

    @Shadow
    protected abstract float handleRotationFloat(T var1, float var2);

    @Shadow
    protected abstract void applyRotations(T var1, float var2, float var3, float var4);

    @Shadow
    public abstract float prepareScale(T var1, float var2);

    @Shadow
    protected abstract void unsetScoreTeamColor();

    @Shadow
    protected abstract boolean setScoreTeamColor(T var1);

    @Shadow
    protected abstract void renderLivingAt(T var1, double var2, double var4, double var6);

    @Shadow
    protected abstract void unsetBrightness();

    @Shadow
    protected abstract void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    protected abstract void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    @Shadow
    protected abstract boolean setDoRenderBrightness(T var1, float var2);

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @author RageMachine222
     * on 11/27/2020
     */
    @Overwrite
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        boolean shouldSit;
        if (MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entity, RenderLivingBase.class.cast((this)), partialTicks, x, y, z)))
            return;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        this.mainModel.isRiding = shouldSit = entity.isRiding() && entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
        this.mainModel.isChild = entity.isChild();
        float f8;
        float f = this.interpolateRotation((entity).prevRenderYawOffset, (entity).renderYawOffset, partialTicks);
        float f1 = this.interpolateRotation((entity).prevRotationYawHead, (entity).rotationYawHead, partialTicks);
        float f2 = f1 - f;
        if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
            f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
            f2 = f1 - f;
            f8 = MathHelper.wrapDegrees(f2);
            if (f8 < -85.0f) {
                f8 = -85.0f;
            }
            if (f8 >= 85.0f) {
                f8 = 85.0f;
            }
            f = f1 - f8;
            if (f8 * f8 > 2500.0f) {
                f += f8 * 0.2f;
            }
            f2 = f1 - f;
        }
        float f7 = (entity).prevRotationPitch + ((entity).rotationPitch - (entity).prevRotationPitch) * partialTicks;
        this.renderLivingAt(entity, x, y, z);
        f8 = this.handleRotationFloat(entity, partialTicks);
        this.applyRotations(entity, f8, f, partialTicks);
        float f4 = this.prepareScale(entity, partialTicks);
        float f5 = 0.0f;
        float f6 = 0.0f;
        if (!entity.isRiding()) {
            f5 = (entity).prevLimbSwingAmount + ((entity).limbSwingAmount - (entity).prevLimbSwingAmount) * partialTicks;
            f6 = (entity).limbSwing - (entity).limbSwingAmount * (1.0f - partialTicks);
            if (entity.isChild()) {
                f6 *= 3.0f;
            }
            if (f5 > 1.0f) {
                f5 = 1.0f;
            }
            f2 = f1 - f;
        }
        GlStateManager.enableAlpha();
        this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
        this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);
        if (this.renderOutlines) {
            boolean flag1 = this.setScoreTeamColor(entity);
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            if (!this.renderMarker) {
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
            }
            if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
            }
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
            if (flag1) {
                this.unsetScoreTeamColor();
            }
        } else {
            if (ColorChams.getINSTANCE().isOn() && ColorChams.getINSTANCE().players.getValue().booleanValue() && entity instanceof EntityPlayer && ColorChams.getINSTANCE().mode.getValue().equals((ColorChams.RenderMode.SOLID))) {
                this.red = ColorChams.getInstance().pRed.getValue() / 255.0f;
                this.green = ColorChams.getInstance().pGreen.getValue() / 255.0f;
                this.blue = ColorChams.getInstance().pBlue.getValue() / 255.0f;
                GlStateManager.pushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                if (MoonHack.getFriendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                    GL11.glColor4f(0.0f, 191.0f, 255.0f, (ColorChams.getINSTANCE().pAlpha.getValue() / 255.0f));
                } else {
                    GL11.glColor4f((ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red), (ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green), (ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue), (ColorChams.getINSTANCE().pAlpha.getValue() / 255.0f));
                }
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                GL11.glDisable(2896);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                if (MoonHack.getFriendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                    GL11.glColor4f(0.0f, 191.0f, 255.0f, (ColorChams.getINSTANCE().pAlpha.getValue() / 255.0f));
                } else {
                    GL11.glColor4f((ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red), (ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green), (ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue), (ColorChams.getINSTANCE().pAlpha.getValue() / 255.0f));
                }
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                GL11.glEnable(2896);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
            boolean flag1 = this.setDoRenderBrightness(entity, partialTicks);
            if (!(entity instanceof EntityPlayer) || ColorChams.getINSTANCE().isOn() && ColorChams.getINSTANCE().mode.getValue().equals((ColorChams.RenderMode.WIREFRAME)) && ColorChams.getINSTANCE().playerModel.getValue().booleanValue() || ColorChams.getINSTANCE().isOff()) {
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
            }
            if (flag1) {
                this.unsetBrightness();
            }
            GlStateManager.depthMask(true);
            if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
            }
            if (ColorChams.getINSTANCE().isOn() && ColorChams.getINSTANCE().players.getValue().booleanValue() && entity instanceof EntityPlayer && ColorChams.getINSTANCE().mode.getValue().equals((ColorChams.RenderMode.WIREFRAME))) {
                this.red = ColorChams.getInstance().pRed.getValue() / 255.0f;
                this.green = ColorChams.getInstance().pGreen.getValue() / 255.0f;
                this.blue = ColorChams.getInstance().pBlue.getValue() / 255.0f;
                GlStateManager.pushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                if (MoonHack.getFriendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                    GL11.glColor4f(0.0f, 191.0f, 255.0f, (ColorChams.getINSTANCE().pAlpha.getValue() / 255.0f));
                } else {
                    GL11.glColor4f((ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red), (ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green), (ColorChams.getInstance().pRainbow.getValue() != false ? ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue), (ColorChams.getINSTANCE().pAlpha.getValue() / 255.0f));
                }
                GL11.glLineWidth(ColorChams.getINSTANCE().lineWidth.getValue());
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                GL11.glEnable(2896);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture( OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(entity, RenderLivingBase.class.cast((this)), partialTicks, x, y, z));
    }
    
    public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
    }


    @Inject(method = "doRender", at = @At("HEAD"))
    public void doRenderPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && entity != null) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    public void doRenderPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && entity != null) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}
