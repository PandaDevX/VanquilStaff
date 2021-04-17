package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnfreezeCommand implements CommandExecutor {

    public UnfreezeCommand(Staff plugin) {
        plugin.getCommand("unfreeze").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(!sender.hasPermission("staff.freeze.others")) {
                sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7You do not have access to that command"));
                return true;
            }
            Utility.sendCorrectArgument(sender, "unfreeze (player)");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!target.hasPlayedBefore()) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + args[0] + " &7did not played before"));
            return true;
        }

        if(!Utility.isFrozen(target)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + target.getName() + " &7is not frozen"));
            return true;
        }

        Utility.unfreezePlayer(target);
        sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + target.getName() + " &7is not frozen anymore"));
        return false;
    }
}
