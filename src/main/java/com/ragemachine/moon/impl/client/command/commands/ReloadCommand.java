package com.ragemachine.moon.impl.client.command.commands;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", new String[]{});
    }

    @Override
    public void execute(String[] commands) {
        MoonHack.reload();
    }
}
