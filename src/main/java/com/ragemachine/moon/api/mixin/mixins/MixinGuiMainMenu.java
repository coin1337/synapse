

package com.ragemachine.moon.api.mixin.mixins;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.client.MainMenu;
import com.ragemachine.moon.impl.util.GLSLSandboxShader;
import com.ragemachine.moon.impl.util.GLSLShaders;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

@Mixin({ GuiMainMenu.class })
public abstract class MixinGuiMainMenu extends GuiScreen {
    @Shadow
    protected abstract void renderSkybox(final int p0, final int p1, final float p2);

    @Shadow
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/title.png");

    @Shadow
    private GuiButton realmsButton;

    @Shadow
    private GuiButton modButton;

    @Shadow
    private GuiButton buttonResetDemo;

    @Shadow
    public void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {

    }


    //Partly Xulu, shaders are all xulu tho since im lazy to add new ones and its not like xulu created them anyways

    @Inject(method = { "initGui" }, at = { @At("RETURN") }, cancellable = true)
    public void initGui(final CallbackInfo info) {
        int j = this.height / 4 + 48;
        try {
            if (MoonHack.getModuleManager.getModule(MainMenu.class).mode.getValue() == MainMenu.Mode.RANDOM) {
                final Random random = new Random();
                final GLSLShaders[] shaders = GLSLShaders.values();
                MoonHack.getBackgroundShader = new GLSLSandboxShader(shaders[random.nextInt(shaders.length)].get());
            }
            else {
                MoonHack.getBackgroundShader = new GLSLSandboxShader(MoonHack.getModuleManager.getModule(MainMenu.class).shader.getValue().get());
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }

        MoonHack.initTime = System.currentTimeMillis();
    }

    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V"))
    private void voided(final GuiMainMenu guiMainMenu, final int mouseX, final int mouseY, final float partialTicks) {
        if (!MoonHack.getModuleManager.getModule(MainMenu.class).isEnabled()) {
            this.renderSkybox(mouseX, mouseY, partialTicks);
        }
    }

    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 0))
    private void noRect1(final GuiMainMenu guiMainMenu, final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        if (!MoonHack.getModuleManager.getModule(MainMenu.class).isEnabled()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 1))
    private void noRect2(final GuiMainMenu guiMainMenu, final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        if (!MoonHack.getModuleManager.getModule(MainMenu.class).isEnabled()) {
            drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Inject(method = { "drawScreen" }, at = { @At("HEAD") }, cancellable = true)
    public void drawScreenShader(final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        if (MoonHack.getModuleManager.getModule(MainMenu.class).isEnabled()) {
            GlStateManager.disableCull();
            MoonHack.getBackgroundShader.useShader(width * 2, height * 2, (float)(mouseX * 2), (float)(mouseY * 2), (System.currentTimeMillis() - MoonHack.initTime) / 1000.0f);
            GL11.glBegin(7);
            GL11.glVertex2f(-1.0f, -1.0f);
            GL11.glVertex2f(-1.0f, 1.0f);
            GL11.glVertex2f(1.0f, 1.0f);
            GL11.glVertex2f(1.0f, -1.0f);
            GL11.glEnd();
            GL20.glUseProgram(0);
        }
    }

    //Momentum thanks :P
    @Inject(
            method = {"drawScreen"},
            at = {@At("TAIL")},
            cancellable = true
    )
    public void drawText(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if  (MoonHack.getModuleManager.getModule(MainMenu.class).isEnabled()) {
            MoonHack.TEXT_MANAGER.drawStringWithShadow("Synapse Utility Mod Beta " + MoonHack.MODVER, 2, 2, new Color(255, 255, 255).getRGB());
            MoonHack.TEXT_MANAGER.drawStringWithShadow("created by RAGE MACHINE a.k.a ManOnTheMoon28", 2, 11, new Color(255, 255, 255).getRGB());
            ResourceLocation location = new ResourceLocation("textures/synapse.png");
            mc.getTextureManager().bindTexture(location);
            GL11.glPushMatrix();
             drawModalRectWithCustomSizedTexture(4, 23, 0.0F, 0.0F, 80, 80, 80.0F, 80.0F);
            GL11.glPopMatrix();
        }
        else {
            return;
        }
    }


}
