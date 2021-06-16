package com.ragemachine.moon.api.mixin;

import com.ragemachine.moon.MoonHack;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class MoonHackMixinLoader implements IFMLLoadingPlugin {

    private static boolean isObfuscatedEnvironment = false;

    public MoonHackMixinLoader() {
        MoonHack.LOGGER.info("Synapse mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.moon.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MoonHack.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (boolean) (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
