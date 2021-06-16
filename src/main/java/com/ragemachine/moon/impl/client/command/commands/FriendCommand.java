package com.ragemachine.moon.impl.client.command.commands;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.manager.FriendManager;
import com.ragemachine.moon.impl.util.TextUtil;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend", new String[]{"<add/del/name/clear>" , "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if(commands.length == 1) {
            if(MoonHack.getFriendManager.getFriends().isEmpty()) {
                sendMessage("You currently dont have any friends added.");
            } else {
                String f = "Friends: ";
                for(FriendManager.Friend friend : MoonHack.getFriendManager.getFriends()) {
                    try {
                        f += friend.getUsername() + ", ";
                    } catch (Exception e) {
                        continue;
                    }
                }
                sendMessage(f);
            }
            return;
        }

        if(commands.length == 2) {
            switch(commands[0]) {
                case "reset":
                    MoonHack.getFriendManager.onLoad();
                    sendMessage("Friends got reset.");
                    break;
                default:
                    sendMessage(commands[0] + (MoonHack.getFriendManager.isFriend(commands[0]) ? " is friended." : " isnt friended."));
                    break;
            }
            return;
        }

        if(commands.length >= 2) {
            switch(commands[0]) {
                case "add" :
                    MoonHack.getFriendManager.addFriend(commands[1]);
                    sendMessage(TextUtil.AQUA + commands[1] + " has been friended");
                    break;
                case "del" :
                    MoonHack.getFriendManager.removeFriend(commands[1]);
                    sendMessage(TextUtil.RED + commands[1] + " has been unfriended");
                    break;
                default :
                    sendMessage(TextUtil.RED + "Bad Command, try: friend <add/del/name> <name>.");
            }
        }
    }
}
