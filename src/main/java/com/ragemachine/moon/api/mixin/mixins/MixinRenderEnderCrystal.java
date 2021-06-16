
package com.ragemachine.moon.api.mixin.mixins;

import javax.annotation.Nullable;

import com.ragemachine.moon.impl.client.modules.render.ColorChams;
import com.ragemachine.moon.impl.util.ColorUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal
extends Render<EntityEnderCrystal> {
    @Shadow
    private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    @Shadow
    private final ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0f, true);
    @Shadow
    private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0f, false);

    protected MixinRenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @author
     */
    @Overwrite
    public void doRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float f = (float)entity.innerRotation + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
        this.bindTexture(ENDER_CRYSTAL_TEXTURES);
        float f1 = MathHelper.sin((float)(f * 0.2f)) / 2.0f + 0.5f;
        f1 += f1 * f1;
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode((int)this.getTeamColor((EntityEnderCrystal) entity));
        }
        if (ColorChams.getINSTANCE().isOn() && ColorChams.getINSTANCE().crystals.getValue().booleanValue()) {
            float red = (float)ColorChams.getInstance().red.getValue().intValue() / 255.0f;
            float green = (float) ColorChams.getInstance().green.getValue().intValue() / 255.0f;
            float blue = (float)ColorChams.getInstance().blue.getValue().intValue() / 255.0f;
            if (ColorChams.getINSTANCE().cMode.getValue().equals((Object)ColorChams.RenderMode.WIREFRAME) && ColorChams.getINSTANCE().crystalModel.getValue()) {
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f * 3.0f, f1 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            GlStateManager.pushMatrix();
            GL11.glPushAttrib((int)1048575);
            if (ColorChams.getINSTANCE().cMode.getValue().equals((Object)ColorChams.RenderMode.WIREFRAME)) {
                GL11.glPolygonMode((int)1032, (int)6913);
            }
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            if (ColorChams.getINSTANCE().cMode.getValue().equals((Object)ColorChams.RenderMode.WIREFRAME)) {
                GL11.glEnable((int)2848);
            }
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)(ColorChams.getInstance().rainbow.getValue() != false ? (float) ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : red), (float)(ColorChams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : green), (float)(ColorChams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : blue), (float)(ColorChams.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
            if (ColorChams.getINSTANCE().cMode.getValue().equals((Object)ColorChams.RenderMode.WIREFRAME)) {
                GL11.glLineWidth((float)ColorChams.getINSTANCE().crystalLineWidth.getValue().floatValue());
            }
            this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f * 3.0f, f1 * 0.2f, 0.0f, 0.0f, 0.0625f);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glColor4f((float)(ColorChams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : red), (float)(ColorChams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : green), (float)(ColorChams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ColorChams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : blue), (float)(ColorChams.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
            if (ColorChams.getINSTANCE().cMode.getValue().equals((Object)ColorChams.RenderMode.WIREFRAME)) {
                GL11.glLineWidth((float)ColorChams.getINSTANCE().crystalLineWidth.getValue().floatValue());
            }
            this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f * 3.0f, f1 * 0.2f, 0.0f, 0.0f, 0.0625f);
            GlStateManager.enableDepth();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        } else {
            this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f * 3.0f, f1 * 0.2f, 0.0f, 0.0f, 0.0625f);
        }
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        BlockPos blockpos = entity.getBeamTarget();
        if (blockpos != null) {
            this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float f2 = (float)blockpos.getX() + 0.5f;
            float f3 = (float)blockpos.getY() + 0.5f;
            float f4 = (float)blockpos.getZ() + 0.5f;
            double d0 = (double)f2 - entity.posX;
            double d1 = (double)f3 - entity.posY;
            double d2 = (double)f4 - entity.posZ;
            RenderDragon.renderCrystalBeams((double)(x + d0), (double)(y - 0.3 + (double)(f1 * 0.4f) + d1), (double)(z + d2), (float)partialTicks, (double)f2, (double)f3, (double)f4, (int)entity.innerRotation, (double)entity.posX, (double)entity.posY, (double)entity.posZ);
        }
        super.doRender((EntityEnderCrystal) entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(EntityEnderCrystal entityEnderCrystal) {
        return null;
    }
}

