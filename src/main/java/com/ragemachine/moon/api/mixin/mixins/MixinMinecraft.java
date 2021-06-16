
package com.ragemachine.moon.api.mixin.mixins;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.client.ClientControl;
import com.ragemachine.moon.impl.client.modules.client.MainMenu;
import com.ragemachine.moon.impl.client.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public WorldClient world;
    @Shadow
    public GuiScreen currentScreen;
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public static final ResourceLocation LOCATION_MOJANG_PNG = new ResourceLocation("textures/loading.png");

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        unload();
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
    public void doVoidFogParticlesHook(WorldClient world, int x, int y, int z) {
        NoRender.getInstance().doVoidFogParticles(x, y, z);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdownHook(CallbackInfo info) {
        unload();
    }

    private void unload() {
        System.out.println("Shutting down: saving configuration");
        MoonHack.onUnload();
        System.out.println("Configuration saved.");
    }

    @Inject(method = { "getLimitFramerate" }, at = { @At("HEAD") }, cancellable = true)
    private void getFrameRate(final CallbackInfoReturnable<Integer> cir) {
        try {
            if (MoonHack.getModuleManager.getModule(MainMenu.class).isEnabled()) {
                cir.setReturnValue((this.world == null && this.currentScreen != null) ? MainMenu.fps.getValue() : Integer.valueOf(this.gameSettings.limitFramerate));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
