package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;

public class CameraClip extends Module {

    public Value<Boolean> extend = addSetting(new Value("Extend", false));
    public Value<Double> distance = addSetting(new Value("Distance", 10.0, 0.0, 50.0, v -> extend.getValue(), "By how much you want to extend the distance."));
    private static CameraClip INSTANCE = new CameraClip();

    public CameraClip() {
        super("CameraClip", "Makes your Camera clip.", Category.RENDER, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static CameraClip getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CameraClip();
        }
        return INSTANCE;
    }
}
