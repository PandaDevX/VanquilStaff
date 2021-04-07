package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
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

        // check if player
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }

        // check arguments

        // if no arguments
        if(args.length == 0) {

            // check permission

            if(!sender.hasPermission("staff.freeze.self")) {
                Utility.sendNoPermissionMessage(sender);
                return true;
            }

            Player player = (Player) sender;

            // check if already frozen
            if(Storage.frozenPlayers.contains(player.getUniqueId().toString())) {

                // unfreeze player
                Storage.frozenPlayers.remove(player.getUniqueId().toString());

                // send alert
                player.sendTitle(Utility.colorize("&6&lFreeze"), Utility.colorize("&aYou are now able to move"), 10, 70, 20);
                return true;
            }

            // freeze self
            Storage.frozenPlayers.add(player.getUniqueId().toString());

            // send alert
            player.sendTitle(Utility.colorize("&6&lFreeze"), Utility.colorize("&aYou are no longer able to move"), 10, 70, 20);

            return true;
        }
        // if player is freezing all

        if(args[0].equalsIgnoreCase("-all")) {

            // check permission

            if(!sender.hasPermission("staff.freeze.all")) {
                Utility.sendNoPermissionMessage(sender);
                return true;
            }

            for(Player online : Bukkit.getOnlinePlayers()) {

                // check if already frozen
                if(Storage.frozenPlayers.contains(online.getUniqueId().toString())) {

                    // unfreeze player
                    Storage.frozenPlayers.remove(online.getUniqueId().toString());

                    // send alert
                    online.sendTitle(Utility.colorize("&6&lFreeze"), Utility.colorize("&aYou are now able to move"), 10, 70, 20);
                    return true;
                }

                // freeze self
                Storage.frozenPlayers.add(online.getUniqueId().toString());

                // send alert
                online.sendTitle(Utility.colorize("&6&lFreeze"), Utility.colorize("&aYou are no longer able to move"), 10, 70, 20);

            }
            return true;
        }

        if(!sender.hasPermission("staff.freeze.others")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        // if target is not online
        if(target == null) {

            // send alert
            sender.sendMessage(Utility.colorize("&c&lHey &fthat player is offline"));
            return true;
        }

        // check if already frozen
        if(Storage.frozenPlayers.contains(target.getUniqueId().toString())) {

            // unfreeze player
            Storage.frozenPlayers.remove(target.getUniqueId().toString());

            // send alert
            target.sendTitle(Utility.colorize("&6&lFreeze"), Utility.colorize("&aYou are now able to move"), 10, 70, 20);
            return true;
        }

        // freeze self
        Storage.frozenPlayers.add(target.getUniqueId().toString());

        // send alert
        target.sendTitle(Utility.colorize("&6&lFreeze"), Utility.colorize("&aYou are no longer able to move"), 10, 70, 20);

        return false;
    }
}
