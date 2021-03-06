package com.vanquil.staff.chat.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FilterAlertCommand implements CommandExecutor {

    public FilterAlertCommand(Staff plugin) {
        plugin.getCommand("falerts").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check permission

        if(!sender.hasPermission("staff.chat.falerts")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        // check if player
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &fyou must be a player to do that"));
            return true;
        }

        Player player = (Player) sender;

        // check if already enabled
        if(Storage.filterAlerts.contains(player.getUniqueId().toString())) {

            // disable alerts
            Storage.filterAlerts.remove(player.getUniqueId().toString());
            player.sendMessage(Utility.colorize("&a&lVanquil &8>>&cDisabled filter alerts"));
            return true;
        }

        // enable alerts
        Storage.filterAlerts.add(player.getUniqueId().toString());

        // send message
        player.sendMessage(Utility.colorize("&a&lVanquil &8>>&aEnabled filter alerts"));
        return false;
    }
}
