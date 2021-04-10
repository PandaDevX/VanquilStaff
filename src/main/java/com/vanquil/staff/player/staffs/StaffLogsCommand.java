package com.vanquil.staff.player.staffs;

import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffLogsCommand implements CommandExecutor {

    public StaffLogsCommand(JavaPlugin plugin) {
        plugin.getCommand("logs").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("staff.logs")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }

        if(Storage.staffLogger.contains(((Player)sender).getUniqueId().toString())) {
            Storage.staffLogger.remove(((Player)sender).getUniqueId().toString());
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Stopped from logging"));
            return true;
        }
        Storage.staffLogger.add(((Player) sender).getUniqueId().toString());

        sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Logging"));
        return false;
    }
}
