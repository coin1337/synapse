package com.ragemachine.moon.impl.client.command.commands;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.util.TextUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", new String[]{"<module>", "<bind>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            sendMessage("Please specify a module.");
            return;
        }

        String rkey = commands[1];
        String moduleName = commands[0];

        Module module = MoonHack.getModuleManager.getModuleByName(moduleName);

        if (module == null){
            sendMessage("Unknown Module '" + TextUtil.RED + module + "'.");
            return;
        }

        if (rkey == null){
            sendMessage(module.getName() + " is currently bound to " + module.getBind().toString());
            return;
        }

        int key = Keyboard.getKeyIndex(rkey.toUpperCase());

        if (rkey.equalsIgnoreCase("none")){
            key = -1;
        }

        if (key == 0){
            sendMessage("Unknown Key '" + rkey + "'!");
            return;
        }

        module.bind.setValue(new Bind(key));
        sendMessage("Bind for " + module.getName() + " set to " + rkey.toUpperCase());
    }
}
