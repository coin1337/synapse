

package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;

import java.awt.*;

public class EnchantColor extends Module
{
    private final Value<Mode> mode;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    public Value<Mode> swingMode;
    
    public EnchantColor() {
        super("EnchantColor", "Changes the color of the enchantment effect",  Category.RENDER, true, false, true);
        this.mode = (Value<Mode>)this.addSetting(new Value("Mode", Mode.COLOR));
        this.red = this.addSetting(new Value("Red", 255, 0, 255));
        this.green = this.addSetting(new Value("Green", 255, 0, 255));
        this.blue = this.addSetting(new Value("Blue",  255, 0, 255));
        swingMode = (Value<Mode>) addSetting(new Value("Swing", Mode.COLOR));;
    }
    
    public static Color getColor(final long offset, final float fade) {
        if (MoonHack.getModuleManager.getModule(EnchantColor.class).mode.getValue() == Mode.COLOR) {
            return new Color(MoonHack.getModuleManager.getModule(EnchantColor.class).red.getValue(), MoonHack.getModuleManager.getModule(EnchantColor.class).green.getValue(), MoonHack.getModuleManager.getModule(EnchantColor.class).blue.getValue());
        }
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public enum Mode {
        COLOR,
        RAINBOW;
    }
}
