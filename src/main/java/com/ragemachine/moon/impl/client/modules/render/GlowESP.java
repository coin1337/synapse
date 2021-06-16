package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.util.Wrapper;
import net.minecraft.entity.Entity;

public class GlowESP extends Module {
    private static GlowESP INSTANCE = new GlowESP();

    public GlowESP() {
        super("GlowESP", "Renders a nice GlowESP.", Category.RENDER, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static GlowESP getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GlowESP();
        }
        return INSTANCE;
    }

    public void onUpdate() {

        for (Entity entity : Wrapper.mc.world.loadedEntityList) {
            if (!entity.isGlowing()) {
                entity.setGlowing(true);

            }
        }

    }

    public void onDisable() {
        for (Entity entity : Wrapper.mc.world.loadedEntityList) {
            if (entity.isGlowing()) {
                entity.setGlowing(false);
            }
        }
        super.onDisable();
    }
}
