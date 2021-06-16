package com.ragemachine.moon.impl.client.command.commands;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;

public class UnloadCommand extends Command {

    public UnloadCommand() {
        super("unload", new String[]{});
    }

    @Override
    public void execute(String[] commands) {
        MoonHack.unload(true);
    }
}
