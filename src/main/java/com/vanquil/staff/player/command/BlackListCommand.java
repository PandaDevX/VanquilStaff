package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlackListCommand implements CommandExecutor {

    public BlackListCommand(Staff plugin) {
        plugin.getCommand("blacklist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check permission

        if(!sender.hasPermission("staff.blacklist")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        // check arguments

        if(args.length < 2) {
            Utility.sendCorrectArgument(sender, "blacklist (player) (reason) [-p]");
            return true;
        }

        // check if player is online
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(Utility.colorize("&c&lTarget &fplayer must not be offline"));
            return true;
        }

        if(args.length == 2) {
            String reason = args[1];

            // add to black list
            Storage.blackListPlayers.put(target.getUniqueId().toString(), reason);
            sender.sendMessage(Utility.colorize("&c&lBlacklist &fPlayer &6" + target.getDisplayName() + " &fhas been added to blacklist players for > &6" + reason));
            return true;
        }

        if(args.length == 3) {
            // if not -p
            if(!args[2].equalsIgnoreCase("-p")) {
                String reason = args[1];

                // add to black list
                Storage.blackListPlayers.put(target.getUniqueId().toString(), reason);
                sender.sendMessage(Utility.colorize("&c&lBlacklist &fPlayer &6" + target.getDisplayName() + " &fhas been added to blacklist players for > &6" + reason));
                return true;
            }
            String reason = args[1];

            // add to black list
            Storage.blackListPlayers.put(target.getUniqueId().toString(), reason);

            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Utility.colorize("&c&lBlacklist &fPlayer &6" + target.getDisplayName() + " &fhas been added to blacklist players for > &6" + reason));
            }
            return true;
        }


        return false;
    }
}
