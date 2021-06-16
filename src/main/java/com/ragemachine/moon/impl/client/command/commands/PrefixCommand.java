package com.ragemachine.moon.impl.client.command.commands;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.util.TextUtil;

public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(TextUtil.RED + "Specify a new prefix.");
            return;
        }

        (MoonHack.getModuleManager.getModule(ClickGui.class)).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to " + TextUtil.GREEN + MoonHack.getCommandManager.getPrefix());
    }
}
