

package com.ragemachine.moon.impl.client.modules.client;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.ClientEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientControl
        extends Module {

    private static ClientControl INSTANCE = new ClientControl();
    public Value<String> commandBracket = this.addSetting(new Value<String>("Bracket", "[- "));
    public Value<String> commandBracket2 = this.addSetting(new Value<String>("Bracket2", "-] "));
    public Value<String> command = this.addSetting(new Value<String>("Name", " Synapse "));
    public Value<TextUtil.Color> bracketColor = this.addSetting(new Value<TextUtil.Color>("Bracket Color", TextUtil.Color.GOLD));
    public Value<TextUtil.Color> commandColor = this.addSetting(new Value<TextUtil.Color>("Name Color", TextUtil.Color.BLUE));
    public Value<String> prefix = addSetting(new Value("Command Prefix", "."));
    public ClientControl() {
        super("Client Control", "Some stuff", Module.Category.CLIENT, false, false, true);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ClientControl getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new ClientControl();
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        MoonHack.getCommandManager.setClientMessage(this.getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() != 2) return;
        if (event.getSetting() == null) return;
        if (!this.equals(event.getSetting().getFeature())) return;
        MoonHack.getCommandManager.setClientMessage(this.getCommandMessage());
    }

    public String getCommandMessage() {
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }

    public String getRawCommandMessage() {
        return this.commandBracket.getValue() + this.command.getValue() + this.commandBracket2.getValue();
    }

    public static enum ThreadMode {
        POOL,
        WHILE,
        NONE;

    }

}

