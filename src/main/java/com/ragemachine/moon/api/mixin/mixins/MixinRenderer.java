package com.ragemachine.moon.api.mixin.mixins;
import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.render.ESP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Render.class)
abstract class MixinRenderer<T extends Entity> {

    @Shadow
    protected abstract int getTeamColor(T entityIn);

    @Shadow
    protected boolean renderOutlines;

    @Shadow
    @Final
    protected RenderManager renderManager;

    @Shadow
    protected abstract boolean bindEntityTexture(T entity);

}