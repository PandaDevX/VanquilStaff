package com.vanquil.staff.player.staffs;

import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffLogsListener implements Listener {

    public StaffLogsListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogCmd(PlayerCommandPreprocessEvent e) {
        if(Utility.getStaffNames().contains(e.getPlayer().getName())) {


            for(Player player : Bukkit.getOnlinePlayers()) {
                if(Storage.staffLogger.contains(player.getUniqueId().toString())) {
                    player.sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &f" + e.getPlayer().getName() + " &b" + e.getMessage()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        if(Utility.getStaffNames().contains(e.getPlayer().getName())) {


            for(Player player : Bukkit.getOnlinePlayers()) {
                if(Storage.staffLogger.contains(player.getUniqueId().toString())) {
                    player.sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &f" + e.getPlayer().getName() + " &bjoined"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerTeleportEvent e) {
        if(Utility.getStaffNames().contains(e.getPlayer().getName())) {


            for(Player player : Bukkit.getOnlinePlayers()) {
                if(Storage.staffLogger.contains(player.getUniqueId().toString())) {
                    player.sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &f" + e.getPlayer().getName() + " &bteleports to a new location"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerQuitEvent e) {
        if(Utility.getStaffNames().contains(e.getPlayer().getName())) {


            for(Player player : Bukkit.getOnlinePlayers()) {
                if(Storage.staffLogger.contains(player.getUniqueId().toString())) {
                    player.sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &f" + e.getPlayer().getName() + " &bleft the server"));
                }
            }
        }
    }
}
