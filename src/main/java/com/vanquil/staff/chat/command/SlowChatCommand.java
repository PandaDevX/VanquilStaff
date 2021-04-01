package com.vanquil.staff.chat.command;

import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SlowChatCommand implements CommandExecutor {

    public SlowChatCommand(JavaPlugin plugin) {
        plugin.getCommand("slowchat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check permission

        if(!sender.hasPermission("staff.chat.slow")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        // check arguments

        if(args.length == 0) {
            Utility.sendCorrectArgument(sender, "slowchat <time (seconds)>");
            return true;
        }

        // check if argument given is number

        if(!Utility.isInt(args[0])) {
            sender.sendMessage(Utility.colorize("&c&lArgument &fgiven must be a number"));
            return true;
        }

        // passed qualifications

        // change the data
        Storage.defaultCD.put("vanquil", Integer.parseInt(args[0]));

        // send succeeding message
        sender.sendMessage(Utility.colorize("&aSuccessfully changed chat interval to &6" + Storage.defaultCD.get("vanquil") + " &asecond(s)"));
        return false;
    }
}
