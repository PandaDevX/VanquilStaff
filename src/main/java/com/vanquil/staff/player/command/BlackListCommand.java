package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(target.isOnline()) {
            target.getPlayer().kickPlayer(Utility.colorize("&c&lHey &fyou have been added to blacklist players"));
            return true;
        }

        if(target.hasPlayedBefore()) {
            sender.sendMessage(Utility.colorize("&c&lBlacklist &fPlayer &c" + args[0] + " &fdid not play the server before"));
            return true;
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        String reason = builder.toString().trim();

        if(reason.endsWith("-p")) {
            // add to black list
            Storage.blackListPlayers.put(target.getUniqueId().toString(), reason);

            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Utility.colorize("&c&lBlacklist &fPlayer &6" + target.getName() + " &fhas been added to blacklist players for > &6" + reason));
            }
            return true;
        }
        // add to black list
        Storage.blackListPlayers.put(target.getUniqueId().toString(), reason);
        sender.sendMessage(Utility.colorize("&c&lBlacklist &fPlayer &6" + target.getName() + " &fhas been added to blacklist players for > &6" + reason));
        return false;
    }
}
