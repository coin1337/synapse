package com.ragemachine.moon.impl.util;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.client.Colors;

import java.awt.*;

public class RainbowUtils {

    private static int rgb;
    public static int a;
    public static int r;
    public static int g;
    public static int b;
    static float hue = 0.01f;

    public static void updateRainbow() {
        rgb = Color.HSBtoRGB(hue, MoonHack.getModuleManager.getModule(Colors.class).rainbowSaturation.getValue()/ 255f, MoonHack.getModuleManager.getModule(Colors.class).rainbowBrightness.getValue() / 255f);
        a = (rgb >>> 24) & 0xFF;
        r = (rgb >>> 16) & 0xFF;
        g = (rgb >>> 8) & 0xFF;
        b = rgb & 0xFF;
        hue += MoonHack.getModuleManager.getModule(Colors.class).rainbowSpeed.getValue() / 1000f;
        if (hue > 1) hue -= 1;
    }
}
