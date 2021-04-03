package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CPSListener implements Listener {
    public CPSListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onListen(PlayerInteractEvent e) {
        boolean leftOnly = Staff.getInstance().getConfig().getBoolean("CPS.left_click_only");

        if(leftOnly) {
            if (e.getAction() != Action.LEFT_CLICK_AIR || e.getAction() != Action.LEFT_CLICK_BLOCK)
                return;
        }

        if (Storage.cpsListeners.isEmpty()) return;
        if (Storage.cpsListeners.contains(e.getPlayer().getUniqueId().toString())) return;

        String uuid = e.getPlayer().getUniqueId().toString();

        if(Storage.clicksInterval.containsKey(uuid)) {
            int diff = (int)(Storage.clicksInterval.get(uuid) - System.currentTimeMillis()) / 1000;

            if(diff <= 0) {
                Storage.clicksInterval.remove(uuid);

                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(Storage.cpsListeners.contains(player.getUniqueId().toString())) {

                        player.sendMessage(Utility.colorize("&c&lCPS> &fDisplaying CPS of &6" + e.getPlayer().getName() + ": &a" + Storage.clicksCount.get(uuid) + " click / second"));
                    }
                }
                Storage.clicksCount.remove(uuid);
            }
        }


        int current = Storage.clicksCount.getOrDefault(uuid, 0);
        Storage.clicksCount.put(uuid, ++current);
        Storage.clicksInterval.put(uuid, System.currentTimeMillis() + 10000L);

    }
}
