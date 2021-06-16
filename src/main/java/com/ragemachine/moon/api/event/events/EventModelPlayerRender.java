package com.ragemachine.moon.api.event.events;

import com.ragemachine.moon.api.event.EventStage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author Elementars
 * @since 8/25/2020 - 4:35 PM
 */
@Cancelable
public class EventModelPlayerRender extends EventStage {

    public ModelBase modelBase;
    public Entity entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scaleFactor;

    public EventModelPlayerRender(ModelBase modelBaseIn, Entity entityIn, float limbSwingIn, float limbSwingAmountIn, float ageInTicksIn, float netHeadYawIn, float headPitchIn, float scaleFactorIn) {
        modelBase = modelBaseIn;
        entity = entityIn;
        limbSwing = limbSwingIn;
        limbSwingAmount = limbSwingAmountIn;
        ageInTicks = ageInTicksIn;
        netHeadYaw = netHeadYawIn;
        headPitch = headPitchIn;
        scaleFactor = scaleFactorIn;
    }
}
