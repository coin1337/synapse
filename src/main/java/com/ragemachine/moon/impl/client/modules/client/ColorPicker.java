package com.ragemachine.moon.impl.client.modules.client;

import com.ragemachine.moon.impl.client.modules.Module;

public class ColorPicker extends Module {
    public ColorPicker() {
        super("Color Picker", "Color Picker", Category.CLIENT, false, false, true);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new com.ragemachine.moon.impl.client.gui.ColorPicker());
    }

    @Override
    public void onDisable() {
        if(mc.currentScreen instanceof com.ragemachine.moon.impl.client.gui.ColorPicker) {
            mc.displayGuiScreen(null);
        }
    }
}
