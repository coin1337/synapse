

package com.ragemachine.moon.api.mixin.mixins;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.render.EnchantColor;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    @Shadow
    private void renderModel(final IBakedModel model, final int color, final ItemStack stack) {
    }
    
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(final int oldValue) {
        return MoonHack.getModuleManager.getModule(EnchantColor.class).isEnabled() ? EnchantColor.getColor(1L, 1.0f).getRGB() : oldValue;
    }

    
    private boolean isHandGood(final EnumHand activeHand, final boolean leftHandedRenderHand) {
        switch (activeHand) {
            case MAIN_HAND: {
                return leftHandedRenderHand;
            }
            case OFF_HAND: {
                return !leftHandedRenderHand;
            }
            default: {
                return false;
            }
        }
    }
}
