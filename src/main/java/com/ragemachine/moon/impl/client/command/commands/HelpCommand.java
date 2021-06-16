package com.ragemachine.moon.impl.client.command.commands;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("commands");
    }

    @Override
    public void execute(String[] commands) {
        sendMessage("You can use following commands: ");
        for(Command command : MoonHack.getCommandManager.getCommands()) {
            sendMessage(MoonHack.getCommandManager.getPrefix() + command.getName());
        }
    }
}
