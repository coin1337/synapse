package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.command.commands.*;
import com.ragemachine.moon.impl.client.modules.client.ClientControl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CommandManager extends Hack {

      //TODO: space at the end of "[-Synapse-] " doesnt save!
    private String clientMessage = "[- Synapse -]";
    private String prefix = ClientControl.getInstance().prefix.getValue();

    //public Value<String> clientMessage = register(new Value("clientMessage", "[-Synapse-]"));
    //public Value<String> prefix = register(new Value("prefix", "."));
    private ArrayList<Command> commands;

    public CommandManager() {
        super("Command");
        commands = new ArrayList<>();
        commands.add(new BindCommand());
        commands.add(new ModuleCommand());
        commands.add(new PrefixCommand());
        commands.add(new FriendCommand());
        commands.add(new HelpCommand());
        commands.add(new ReloadCommand());
        commands.add(new UnloadCommand());
        commands.add(new ReloadSoundCommand());
        commands.add(new CrashCommand());
        commands.add(new HistoryCommand());
    }

    public void executeCommand(String command){
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String name = parts[0].substring(1);
        String[] args = removeElement(parts, 0);
        for (int i = 0; i < args.length; i++){
            if (args[i] == null) continue;
            args[i] = strip(args[i], "\"");
        }
        for (Command c : commands){
            if (c.getName().equalsIgnoreCase(name)){
                c.execute(parts);
                return;
            }
        }
        Command.sendMessage("Unknown command. try 'commands' for a list of commands.");
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        List result = new LinkedList();
        for (int i = 0; i < input.length; i++){
            if (i != indexToDelete) result.add(input[i]);
        }
        return (String[]) result.toArray(input);
    }

    private static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) return str.substring(key.length(), str.length()-key.length());
        return str;
    }

    public Command getCommandByName(String name){
        for (Command command : commands){
            if (command.getName().equals(name)) {
                return command;
            }
        }
        return null;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
