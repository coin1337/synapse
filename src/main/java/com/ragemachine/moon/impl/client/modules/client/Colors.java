
package com.ragemachine.moon.impl.client.modules.client;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.ColorUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colors
        extends Module {
    private static Colors instance;
    public Value<Boolean> rainbow = this.addSetting(new Value<Boolean>("Rainbow", true, "Rainbow colors."));
    public Value<Integer> rainbowSpeed = this.addSetting(new Value<Object>("Speed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Value<Integer> rainbowSaturation = this.addSetting(new Value<Object>("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Value<Integer> rainbowBrightness = this.addSetting(new Value<Object>("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Value<Integer> red = this.addSetting(new Value<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> {
        if (this.rainbow.getValue() != false) return false;
        return true;
    }));
    public Value<Integer> green = this.addSetting(new Value<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> {
        if (this.rainbow.getValue() != false) return false;
        return true;
    }));
    public Value<Integer> blue = this.addSetting(new Value<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> {
        if (this.rainbow.getValue() != false) return false;
        return true;
    }));
    public Value<Integer> alpha = this.addSetting(new Value<Object>("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> {
        if (this.rainbow.getValue() != false) return false;
        return true;
    }));
    public float hue;
    public Map<Integer, Integer> colorHeightMap = new HashMap<Integer, Integer>();
    public static Colors INSTANCE;

    public Colors() {
        super("Colors", "Universal colors.", Module.Category.CLIENT, true, false, true);
        instance = this;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Colors getInstance() {
        if (Colors.instance == null) {
            Colors.instance = new Colors();
        }
        return Colors.instance;
    }

    @Override
    public void onTick() {
        int colorSpeed = 101 - this.rainbowSpeed.getValue();
        float tempHue = this.hue = (float)(System.currentTimeMillis() % (long)(360 * colorSpeed)) / (360.0f * (float)colorSpeed);
        int i = 0;
        do {
            if (i > 510) {
                if (    ClickGui.getInstance().colorSync.getValue() == false) return;
                MoonHack.COLOR_MANAGER.setColor(getInstance().getCurrentColor().getRed(), getInstance().getCurrentColor().getGreen(), getInstance().getCurrentColor().getBlue(), ClickGui.getInstance().hoverAlpha.getValue());
                return;
            }
            this.colorHeightMap.put(i, Color.HSBtoRGB(tempHue, (float)this.rainbowSaturation.getValue().intValue() / 255.0f, (float)this.rainbowBrightness.getValue().intValue() / 255.0f));
            tempHue += 0.0013071896f;
            ++i;
        } while (true);
    }

    public int getCurrentColorHex() {
        if (this.rainbow.getValue() == false) return ColorUtil.toARGB(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
        return Color.HSBtoRGB(this.hue, (float)this.rainbowSaturation.getValue().intValue() / 255.0f, (float)this.rainbowBrightness.getValue().intValue() / 255.0f);
    }

    public Color getCurrentColor() {
        if (this.rainbow.getValue() == false) return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
        return Color.getHSBColor(this.hue, (float)this.rainbowSaturation.getValue().intValue() / 255.0f, (float)this.rainbowBrightness.getValue().intValue() / 255.0f);
    }
}

