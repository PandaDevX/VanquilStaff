package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffHideCommand implements CommandExecutor {

    public StaffHideCommand(JavaPlugin plugin) {
        plugin.getCommand("hidestaff").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("staff.hidestaff")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &fyou must be a player to do that"));
            return true;
        }

        if(Storage.hideStaffs.contains(((Player)sender).getUniqueId().toString())) {
            Storage.hideStaffs.remove(((Player)sender).getUniqueId().toString());
            sender.sendMessage(Utility.colorize("&a&lVanquil HideStaffs &8>> &7Disabled"));

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(Utility.getStaffNames().contains(player.getName())) {
                    ((Player)sender).hidePlayer(Staff.getInstance(), player);
                }
            }
            return true;
        }
        Storage.hideStaffs.add(((Player) sender).getUniqueId().toString());

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(Utility.getStaffNames().contains(player.getName())) {
                ((Player)sender).showPlayer(Staff.getInstance(), player);
            }
        }

        sender.sendMessage(Utility.colorize("&a&lVanquil HideStaffs &8>> &7Enabled"));
        return false;
    }
}
