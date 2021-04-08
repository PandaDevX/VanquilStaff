package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Iterator;
import java.util.Set;

public class StaffJoin implements Listener {

    public StaffJoin(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "staffs.yml", true);
        Set<String> staffs = fileUtil.get().getConfigurationSection("Staffs").getKeys(false);
        if(staffs.contains(e.getPlayer().getName())) {

            e.setJoinMessage(null);

            for(String staff : staffs) {

                OfflinePlayer player = Bukkit.getOfflinePlayer(staff);

                if(player.isOnline()) {
                    for(String message : fileUtil.get().getStringList("join")) {
                        player.getPlayer().sendMessage(Utility.colorize(message.replace("{staff}", e.getPlayer().getName())));
                    }
                }
                player = null;
            }
        }
        fileUtil = null;
        staffs = null;
    }
}
