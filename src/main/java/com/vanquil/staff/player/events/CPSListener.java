package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CPSListener implements Listener {
    public CPSListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onListen(PlayerInteractEvent e) {
        if(Storage.cpsListeners.isEmpty()) return;
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            int current = Storage.clicksCount.getOrDefault(e.getPlayer().getUniqueId().toString(), 0);
            Storage.clicksCount.put(e.getPlayer().getUniqueId().toString(), ++current);
            if (!Storage.clicksInterval.containsKey(e.getPlayer().getUniqueId().toString())) {
                Storage.clicksInterval.put(e.getPlayer().getUniqueId().toString(), System.currentTimeMillis() + (10 * 1000));
            }
        }

    }

    @EventHandler
    public void onClick(EntityDamageByEntityEvent e) {
        if(Storage.cpsListeners.isEmpty()) return;
        if(e.getDamager() instanceof Player) {

            Player player = (Player) e.getDamager();

            int current = Storage.clicksCount.getOrDefault(player.getUniqueId().toString(), 0);
            Storage.clicksCount.put(player.getUniqueId().toString(), ++current);
            if(!Storage.clicksInterval.containsKey(player.getUniqueId().toString())) {
                Storage.clicksInterval.put(player.getUniqueId().toString(), System.currentTimeMillis() + (10 * 1000));
            }
        }
    }
}
