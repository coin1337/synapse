package com.ragemachine.moon.impl.client.command.commands;

import com.google.gson.JsonParser;
import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.manager.ConfigManager;
import com.ragemachine.moon.impl.util.TextUtil;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", new String[]{"<module>", "<set/reset>", "<value>", "<value>"});
    }

    @Override
    public void execute(String[] commands) {
        if(commands.length == 1) {
            sendMessage("Modules: ");
            for(Module.Category category : MoonHack.getModuleManager.getCategories()) {
                String modules = category.getName() + ": ";
                for (Module module : MoonHack.getModuleManager.getModulesByCategory(category)) {
                    modules += (module.isEnabled() ? TextUtil.GREEN : TextUtil.RED) + module.getName() + TextUtil.RESET + ", ";
                }
                sendMessage(modules);
            }
            return;
        }

        Module module = MoonHack.getModuleManager.getModuleByDisplayName(commands[0]);
        if(module == null) {
            module = MoonHack.getModuleManager.getModuleByName(commands[0]);
            if(module == null) {
                sendMessage(TextUtil.RED + "This module doesnt exist.");
                return;
            }
            sendMessage(TextUtil.RED + " This is the original name of the module. Its current name is: " + module.getDisplayName());
            return;
        }

        if(commands.length == 2) {
            sendMessage(module.getDisplayName() + " : " + module.getDescription());
            for(Value value : module.getSettings()) {
                sendMessage(value.getName() + " : " + value.getValue() + ", " + value.getDescription());
            }
            return;
        }

        if(commands.length == 3) {
            if (commands[1].equalsIgnoreCase("set")) {
                sendMessage(TextUtil.RED + "Please specify a value.");
            } else if (commands[1].equalsIgnoreCase("reset")) {
                for(Value value : module.getSettings()) {
                    value.setValue(value.getDefaultValue());
                }
            } else {
                sendMessage(TextUtil.RED + "This command doesnt exist.");
            }
            return;
        }

        if(commands.length == 4) {
            sendMessage(TextUtil.RED + "Please specify a value.");
            return;
        }

        if(commands.length == 5) {
            Value value = module.getSettingByName(commands[2]);
            if (value != null) {
                JsonParser jp = new JsonParser();
                if(value.getType().equalsIgnoreCase("String")) {
                    value.setValue(commands[3]);
                    sendMessage(TextUtil.GREEN + module.getName() + " " + value.getName() + " has been set to " + commands[3] + ".");
                    return;
                }
                try {
                    if(value.getName().equalsIgnoreCase("Enabled")) {
                        if(commands[3].equalsIgnoreCase("true")) {
                            module.enable();
                        }
                        if(commands[3].equalsIgnoreCase("false")) {
                            module.disable();
                        }
                    }
                    ConfigManager.setValueFromJson(module, value, jp.parse(commands[3]));
                } catch (Exception e) {
                    sendMessage(TextUtil.RED + "Bad Value! This value requires a: " + value.getType() + " value.");
                    return;
                }
                sendMessage(TextUtil.GREEN + module.getName() + " " + value.getName() + " has been set to " + commands[3] + ".");
            }
        }
    }
}
