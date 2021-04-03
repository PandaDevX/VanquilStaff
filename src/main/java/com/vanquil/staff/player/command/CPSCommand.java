package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CPSCommand implements CommandExecutor {

    public CPSCommand(Staff plugin) {
        plugin.getCommand("cps").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check if player
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }

        // check permission

        if(!sender.hasPermission("staff.cps")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }
        Player player = (Player) sender;
        // check if player is already listening to all cps
        if(Storage.cpsListeners.contains(player.getUniqueId().toString()) && Storage.cpsTaskID.containsKey(player.getUniqueId().toString())) {

            Storage.cpsListeners.remove(player.getUniqueId().toString());
            Bukkit.getScheduler().cancelTask(Storage.cpsTaskID.get(player.getUniqueId().toString()));
            Storage.cpsTaskID.remove(player.getUniqueId().toString());

            sender.sendMessage(Utility.colorize("&aSuccessfully abort listening to all CPS"));

            return true;
        }
        Storage.cpsListeners.add(player.getUniqueId().toString());
        // run task
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Storage.cpsListeners.remove(player.getUniqueId().toString());
                Storage.cpsTaskID.remove(player.getUniqueId().toString());
            }
        };
        runnable.runTaskLater(Staff.getInstance(), (60 * 20));
        Storage.cpsTaskID.put(player.getUniqueId().toString(), runnable.getTaskId());

        sender.sendMessage(Utility.colorize("&c&lCPS>> &fStarted listening"));

        runnable = null;
        return false;
    }
}
