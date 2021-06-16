package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.EventStage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author Elementars
 * @since 8/6/2020 - 1:12 PM
 */
@Cancelable
public class EventPostRenderLayers extends EventStage {

    public RenderLivingBase renderer;
    public ModelBase modelBase;
    public EntityLivingBase entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float partialTicks;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scaleIn;

    public RenderLivingBase getRenderer() {
        return renderer;
    }

    public EventPostRenderLayers(RenderLivingBase renderer, ModelBase modelBase, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        this.renderer = renderer;
        this.modelBase = modelBase;
        this.entity = entitylivingbaseIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.partialTicks = partialTicks;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleIn = scaleIn;
    }

    public ModelBase getModelBase() {
        return modelBase;
    }

    public EntityLivingBase getEntitylivingbaseIn() {
        return entity;
    }

    public float getLimbSwing() {
        return limbSwing;
    }

    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getAgeInTicks() {
        return ageInTicks;
    }

    public float getNetHeadYaw() {
        return netHeadYaw;
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public float getScaleIn() {
        return scaleIn;
    }
}
