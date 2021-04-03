package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
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
        for(String staff : staffs) {

            if(e.getPlayer().getName().equals(staff)) {

                e.setJoinMessage(null);
                for (String name : staffs) {
                    if(name.equals(e.getPlayer().getName()))
                        continue;
                    Player target = Bukkit.getPlayer(name);
                    target.sendMessage(Utility.colorize("&6Staff> &c" + target.getDisplayName() + " &fhas joined"));
                    target = null;
                }
                break;
            }
        }
        fileUtil = null;
        staffs = null;
    }
}
