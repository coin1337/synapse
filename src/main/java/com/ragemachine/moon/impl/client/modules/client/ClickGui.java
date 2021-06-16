package com.ragemachine.moon.impl.client.modules.client;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.ClientEvent;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui extends Module {
    public Value<Boolean> colorSync = this.addSetting(new Value<Boolean>("Sync", true));
    public Value<Boolean> rainbowRolling = this.addSetting(new Value<Object>("RollingRainbow", Boolean.valueOf(false), v -> {
        if (!this.colorSync.getValue()) return false;
        if (!Colors.getInstance().rainbow.getValue()) return false;
        return true;
    }));

    public Value<String> prefix = addSetting(new Value("Prefix", "."));
    public Value<Integer> red = addSetting(new Value("Red", 255, 0, 255));
    public Value<Integer> green = addSetting(new Value("Green", 0, 0, 255));
    public Value<Integer> blue = addSetting(new Value("Blue", 0, 0, 255));
    public Value<Integer> hoverAlpha = addSetting(new Value("Alpha", 180, 0, 255));
    public Value<Integer> alpha = addSetting(new Value("HoverAlpha", 240, 0, 255));
    public Value<Boolean> customFov = addSetting(new Value("CustomFov", false));
    public Value<Float> fov = addSetting(new Value("Fov", 150.0f, -180.0f, 180.0f, v -> customFov.getValue()));
    public Value<String> moduleButton = addSetting(new Value("Buttons", ""));
    public Value<Boolean> devSettings = addSetting(new Value("TopBarSettings", false));
    public Value<Integer> topRed = addSetting(new Value("TopRed", 255, 0, 255, v -> devSettings.getValue()));
    public Value<Integer> topGreen = addSetting(new Value("TopGreen", 0, 0, 255, v -> devSettings.getValue()));
    public Value<Integer> topBlue = addSetting(new Value("TopBlue", 0, 0, 255, v -> devSettings.getValue()));
    public Value<Integer> topAlpha = addSetting(new Value("TopAlpha", 255, 0, 255, v -> devSettings.getValue()));
    public Value<Boolean> shader = addSetting(new Value("Blur", true));
    public Value<Boolean> textShadow = addSetting(new Value("Font Shadow", true));
    public Value<Boolean> watermark = addSetting(new Value("Watermark", true));
    public Value<Integer> watermarkScale = addSetting(new Value("Watermark Scale", 50, 0, 400, v -> watermark.getValue()));
    public Value<Integer> watermarkX = addSetting(new Value("Watermark X", 700, 0, 1000, v -> watermark.getValue()));
    public Value<Integer> watermarkY = addSetting(new Value("Watermark Y", 330, 0, 1000, v -> watermark.getValue()));
    public Value<Integer> sizeWidth = addSetting(new Value("Width", 20, 0, 50 ));
    public Value<Float> olWidth = addSetting(new Value("Outline Width", 0.0f, 1f, 5f));
    public Value<Integer> textSize = addSetting(new Value("Text Size", 21, "FontSize"));


    private static ClickGui INSTANCE = new ClickGui();

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Category.CLIENT, true, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ClickGui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (customFov.getValue()) {
            mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov.getValue());
        }
        if (shader.getValue() == true) {
            if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (mc.entityRenderer.shaderGroup != null) {
                    mc.entityRenderer.shaderGroup.deleteShaderGroup();
                }
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
        if (shader.getValue() == false) {
            if (mc.entityRenderer.shaderGroup != null) {
                mc.entityRenderer.shaderGroup.deleteShaderGroup();
                mc.entityRenderer.shaderGroup = null;
            }
        }
    }

    public static ClickGui getGui() {
        return getInstance();
    }

    public float getOutlineWidth() {
        return olWidth.getValue();
    }

    public float getSizeWidth() {
        return sizeWidth.getValue();
    }



    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if(event.getStage() == 2) {
            if(event.getSetting().getFeature().equals(this)) {
                if(event.getSetting().equals(this.prefix)) {
                    MoonHack.getCommandManager.setPrefix(this.prefix.getPlannedValue());
                    Command.sendMessage("Prefix set to " + TextUtil.BLUE + MoonHack.getCommandManager.getPrefix());
                }
                MoonHack.COLOR_MANAGER.setColor(red.getPlannedValue(), green.getPlannedValue(), blue.getPlannedValue(), hoverAlpha.getPlannedValue());
            }
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new MoonHackGui());
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue().booleanValue()) {
            MoonHack.COLOR_MANAGER.setColor(Colors.getInstance().getCurrentColor().getRed(), Colors.getInstance().getCurrentColor().getGreen(), Colors.getInstance().getCurrentColor().getBlue(), this.hoverAlpha.getValue());
        } else {
            MoonHack.COLOR_MANAGER.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        }
        MoonHack.getCommandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if(!(mc.currentScreen instanceof MoonHackGui)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        mc.entityRenderer.shaderGroup = null;
        if(mc.currentScreen instanceof MoonHackGui) {
            mc.displayGuiScreen(null);
        }
    }

    public void shaderOn() {
        if (shader.getValue() == false) {
            mc.entityRenderer.shaderGroup = null;
        }
    }
}
