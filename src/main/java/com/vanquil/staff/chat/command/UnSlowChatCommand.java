package com.vanquil.staff.chat.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnSlowChatCommand implements CommandExecutor {

    private Staff plugin;
    public UnSlowChatCommand(Staff plugin) {
        plugin.getCommand("unslow").setExecutor(this);
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check permission

        if(!sender.hasPermission("staff.chat.unslow")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        //qualifications passed

        // change the data
        Storage.defaultCD.remove("vanquil");

        // send succeeding message
        sender.sendMessage(Utility.colorize("&aSuccessfully changed chat interval to &6default"));
        return false;
    }
}
