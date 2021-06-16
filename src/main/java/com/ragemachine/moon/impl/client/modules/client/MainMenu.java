

package com.ragemachine.moon.impl.client.modules.client;


import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.GLSLShaders;

public class MainMenu extends Module
{

    public Value<Mode> mode;
    public Value<GLSLShaders> shader;
    public static Value<Integer> fps;
    public Value<Boolean> logo;
    public Value<Boolean> watermark;

    public MainMenu() {
        super("Main Menu", "Displays cool graphics for the main menu background", Category.CLIENT, true, false, true);
        this.mode = this.addSetting(new Value("Mode",  Mode.RANDOM));
        this.shader = this.addSetting(new Value("Shader", GLSLShaders.ICYFIRE));
        this.logo = addSetting(new Value("Synapse Watermark", true));


    }
    public enum Mode {
        RANDOM,
        SELECT;
    }
}
