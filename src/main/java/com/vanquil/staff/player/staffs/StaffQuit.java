package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Set;

public class StaffQuit implements Listener {

    public StaffQuit(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Set<String> staffs = Storage.staffLogger;
        if(staffs.contains(e.getPlayer().getName())) {

            e.setQuitMessage(null);

            for(String staff : staffs) {

                OfflinePlayer player = Bukkit.getOfflinePlayer(staff);

                if(player.isOnline()) {
                    if(player.getPlayer().equals(e.getPlayer()))
                        continue;
                    player.getPlayer().sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &3" + e.getPlayer().getName() + " &7has gone &coffline"));
                }
                player = null;
            }
        }
        staffs = null;
    }
}
