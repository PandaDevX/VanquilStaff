package com.vanquil.staff.vanquilstaff;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    public MainCommand(Staff plugin) {
        plugin.getCommand("vanquilstaff").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if(!sender.hasPermission("staff.maincmd")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }
        if(args.length == 0) {
            Utility.sendCorrectArgument(sender, label + " <help|reload>");
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            Staff.getInstance().reloadConfig();
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &fConfig &7reloaded successfully"));
            return true;
        }


        return false;
    }
}
