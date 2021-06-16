package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;

public class SmallShield extends Module {

    public Value<Boolean> normalOffset = addSetting(new Value("OffNormal", false));
    public Value<Float> offset = addSetting(new Value("Offset", 0.7f, 0.0f, 1.0f, v -> normalOffset.getValue()));
    public Value<Float> offX = addSetting(new Value("OffX", 0.0f, -1.0f, 1.0f, v -> !normalOffset.getValue()));
    public Value<Float> offY = addSetting(new Value("OffY", 0.0f, -1.0f, 1.0f, v -> !normalOffset.getValue()));
    public Value<Float> mainX = addSetting(new Value("MainX", 0.0f, -1.0f, 1.0f));
    public Value<Float> mainY = addSetting(new Value("MainY", 0.0f, -1.0f, 1.0f));

    private static SmallShield INSTANCE = new SmallShield();

    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Category.RENDER, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if(normalOffset.getValue()) {
            mc.entityRenderer.itemRenderer.equippedProgressOffHand = offset.getValue();
        }
    }

    public static SmallShield getINSTANCE() {
        if(INSTANCE == null) {
            INSTANCE = new SmallShield();
        }
        return INSTANCE;
    }
}
