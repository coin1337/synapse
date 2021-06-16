
package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ColorChams
extends Module {
    public Value<RenderMode> mode = addSetting(new Value<RenderMode>("PMode", RenderMode.SOLID));
    public Value<RenderMode> cMode = addSetting(new Value<RenderMode>("CMode", RenderMode.SOLID));
    public Value<Boolean> players = addSetting(new Value("Players", false));
    public Value <Boolean>playerModel = addSetting(new Value("PlayerModel", false));
    public Value<Boolean> crystals = addSetting(new Value("Crystals", false));
    public Value<Boolean> crystalModel = addSetting(new Value("CrystalModel", false));
    public Value<Integer> red = addSetting(new Value("Red", 255, 0, 255));
    public Value<Integer> green = addSetting(new Value("Green", 255, 0, 255));
    public Value<Integer> blue = addSetting(new Value("Blue", 255, 0, 255));
    public final Value<Float> alpha = addSetting(new Value<Float>("Crystal Alpha", 255.0f, 0.1f, 255.0f));
    public Value<Boolean> rainbow = addSetting(new Value("Crystal Rainbow", false));

    public Value<Integer> pRed = addSetting(new Value("Player Red", 255, 0, 255));
    public Value<Integer> pGreen = addSetting(new Value("Player Green", 255, 0, 255));
    public Value<Integer> pBlue = addSetting(new Value("Player Blue", 255, 0, 255));
    public final Value<Float> pAlpha = addSetting(new Value<Float>("Player Alpha", 255.0f, 0.1f, 255.0f));
    public Value<Boolean> pRainbow = addSetting(new Value("Player Rainbow", false));

    public Value<Integer> rainbowHue = addSetting(new Value("Delay", 240, 0, 600, v -> false));
    public Value<Float> rainbowBrightness = addSetting(new Value("Brightness ", 150.0f, 1.0f, 255.0f, v -> false));
    public Value<Float> rainbowSaturation = addSetting(new Value("Saturation", 150.0f, 1.0f, 255.0f, v -> false));

    public final Value<Float> lineWidth = addSetting(new Value<Float>("PLineWidth", 1.0f, 0.1f, 3.0f));
    public final Value<Float> crystalLineWidth = addSetting(new Value<Float>("CLineWidth", 1.0f, 0.1f, 3.0f));
    private static ColorChams INSTANCE = new ColorChams();

    public ColorChams() {
        super("ColorChams", "Draws a wireframe esp around other players.", Module.Category.RENDER, false, false, false);
        setInstance();
    }

    @SubscribeEvent
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {
        event.getEntityPlayer().hurtTime = 0;
    }

    public static ColorChams getINSTANCE() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new ColorChams();
        return INSTANCE;
    }

    public static enum RenderMode {
        SOLID,
        WIREFRAME;
        
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ColorChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ColorChams();
        }
        return INSTANCE;
    }

    static {
        INSTANCE = new ColorChams();
    }
}

