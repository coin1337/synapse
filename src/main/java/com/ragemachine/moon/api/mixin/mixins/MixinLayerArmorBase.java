package com.ragemachine.moon.api.mixin.mixins;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.render.EnchantColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ LayerArmorBase.class })
public class MixinLayerArmorBase {
    @Redirect(method = {"renderEnchantedGlint"}, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", ordinal = 1))
    private static void renderEnchantedGlint(final float red, final float green, final float blue, final float alpha) {
        GlStateManager.color(MoonHack.getModuleManager.getModule(EnchantColor.class).isEnabled() ? ((float) EnchantColor.getColor(1L, 1.0f).getRed()) : red, MoonHack.getModuleManager.getModule(EnchantColor.class).isEnabled() ? ((float) EnchantColor.getColor(1L, 1.0f).getGreen()) : green, MoonHack.getModuleManager.getModule(EnchantColor.class).isEnabled() ? ((float) EnchantColor.getColor(1L, 1.0f).getBlue()) : blue, MoonHack.getModuleManager.getModule(EnchantColor.class).isEnabled() ? ((float) EnchantColor.getColor(1L, 1.0f).getAlpha()) : alpha);
    }
}