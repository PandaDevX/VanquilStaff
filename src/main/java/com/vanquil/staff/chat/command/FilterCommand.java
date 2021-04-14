package com.vanquil.staff.chat.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FilterCommand implements CommandExecutor {
    private Staff plugin;

    public FilterCommand(Staff plugin) {
        this.plugin = plugin;
        plugin.getCommand("filter").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check permission

        if(!sender.hasPermission("staff.chat.filter")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        // open storage
        FileUtil fileUtil = new FileUtil(plugin, "words.yml", true);

        // check if already enabled
        if(fileUtil.get().getBoolean("Enable")) {

            // disable
            fileUtil.get().set("Enable", false);
            fileUtil.save();
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>>&aSuccessfully disabled filter system"));
            return true;
        }

        // enable
        fileUtil.get().set("Enable", true);
        fileUtil.save();
        sender.sendMessage(Utility.colorize("&a&lVanquil &8>>&aSuccessfully enabled filter system"));

        // close storage
        fileUtil = null;
        return false;
    }
}
