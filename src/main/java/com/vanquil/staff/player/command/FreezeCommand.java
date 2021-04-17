package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {

    public FreezeCommand(Staff plugin) {
        plugin.getCommand("freeze").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length < 2) {
            if(!sender.hasPermission("staff.freeze.others")) {
                sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7You do not have access to that command"));
                return true;
            }
            Utility.sendCorrectArgument(sender, "freeze (player) (duration)");
            sender.sendMessage(Utility.colorize("&7[format: 1<suffix>] [suffixes: y - year, M - month, w - week, d - day, h - hour, m - minute, s - second]"));
            sender.sendMessage(Utility.colorize("&7-1 for permanent"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Error: &fplayer must be online"));
            return true;
        }
        if(args[1].equalsIgnoreCase("-1")) {
            if(Utility.isFrozen(target)) {
                sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + target.getName() + " &7is already frozen"));
                return true;
            }
            Utility.freezePlayer(target);
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + target.getName() + " &7has been frozen"));
            return true;
        }
        if(Utility.isFrozen(target)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + target.getName() + " &7is already frozen"));
            return true;
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        String duration = builder.toString().trim();
        Utility.freezePlayer(target, duration);
        sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &7Player &f" + target.getName() + " &7has been frozen for duration: &f" + duration));
        return false;
    }
}
