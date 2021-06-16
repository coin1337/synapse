package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.EventStage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EventModelRender
        extends EventStage {

    public ModelBase modelBase;
    public Entity entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scaleFactor;

    public EventModelRender(ModelBase modelBaseIn, Entity entityIn, float limbSwingIn, float limbSwingAmountIn, float ageInTicksIn, float netHeadYawIn, float headPitchIn, float scaleFactorIn) {
        modelBase = modelBaseIn;
        entity = entityIn;
        limbSwing = limbSwingIn;
        limbSwingAmount = limbSwingAmountIn;
        ageInTicks = ageInTicksIn;
        netHeadYaw = netHeadYawIn;
        headPitch = headPitchIn;
        scaleFactor = scaleFactorIn;
    }

    private float partialTicks;

    public EventModelRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
