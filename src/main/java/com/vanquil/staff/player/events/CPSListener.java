package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CPSListener implements Listener {
    public CPSListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onListen(PlayerInteractEvent e) {
        try {
            if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        }catch (NoSuchMethodError ex) {

        }
        if(Storage.cpsListeners.isEmpty()) {
            Storage.nextClick.remove(e.getPlayer().getUniqueId().toString());
            Storage.clicksCount.remove(e.getPlayer().getUniqueId().toString());
            Storage.clicksInterval.remove(e.getPlayer().getUniqueId().toString());
            Storage.clicksCountRaw.remove(e.getPlayer().getUniqueId().toString());
            return;
        }
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if(!Storage.nextClick.containsKey(e.getPlayer().getUniqueId().toString())) {
                Storage.nextClick.put(e.getPlayer().getUniqueId().toString(), System.currentTimeMillis() + 1000);
            }
            long time = Storage.nextClick.get(e.getPlayer().getUniqueId().toString());
            if(time <= System.currentTimeMillis()) {
                // second passed
                Storage.nextClick.remove(e.getPlayer().getUniqueId().toString());
                if(Storage.clicksCountRaw.containsKey(e.getPlayer().getUniqueId().toString())) {
                    String current = Storage.clicksCountRaw.get(e.getPlayer().getUniqueId().toString());
                    Storage.clicksCountRaw.put(e.getPlayer().getUniqueId().toString(), current + "," + Storage.clicksCount.get(e.getPlayer().getUniqueId().toString()));
                } else {
                    Storage.clicksCountRaw.put(e.getPlayer().getUniqueId().toString(), Storage.clicksCount.get(e.getPlayer().getUniqueId().toString()) + "");
                }
                // reset count
                Storage.clicksCount.remove(e.getPlayer().getUniqueId().toString());

            }
            int current = Storage.clicksCount.getOrDefault(e.getPlayer().getUniqueId().toString(), 0);
            Storage.clicksCount.put(e.getPlayer().getUniqueId().toString(), ++current);
            if (!Storage.clicksInterval.containsKey(e.getPlayer().getUniqueId().toString())) {
                Storage.clicksInterval.put(e.getPlayer().getUniqueId().toString(), System.currentTimeMillis() + (10 * 1000));
            }
        }

    }

    @EventHandler
    public void onClick(EntityDamageByEntityEvent e) {

        if(e.getDamager() instanceof Player) {

            Player player = (Player) e.getDamager();

            if(Storage.cpsListeners.isEmpty()) {
                Storage.nextClick.remove(player.getUniqueId().toString());
                Storage.clicksCount.remove(player.getUniqueId().toString());
                Storage.clicksInterval.remove(player.getUniqueId().toString());
                Storage.clicksCountRaw.remove(player.getUniqueId().toString());
                return;
            }

            if(!Storage.nextClick.containsKey(player.getUniqueId().toString())) {
                Storage.nextClick.put(player.getUniqueId().toString(), System.currentTimeMillis() + 1000);
            }
            long time = Storage.nextClick.get(player.getUniqueId().toString());
            if(time <= System.currentTimeMillis()) {
                // second passed
                Storage.nextClick.remove(player.getUniqueId().toString());
                if(Storage.clicksCountRaw.containsKey(player.getUniqueId().toString())) {
                    String current = Storage.clicksCountRaw.get(player.getUniqueId().toString());
                    Storage.clicksCountRaw.put(player.getUniqueId().toString(), current + "," + Storage.clicksCount.get(player.getUniqueId().toString()));
                } else {
                    Storage.clicksCountRaw.put(player.getUniqueId().toString(), Storage.clicksCount.get(player.getUniqueId().toString()) + "");
                }
                // reset count
                Storage.clicksCount.remove(player.getUniqueId().toString());
            }
            int current = Storage.clicksCount.getOrDefault(player.getUniqueId().toString(), 0);
            Storage.clicksCount.put(player.getUniqueId().toString(), ++current);
            if(!Storage.clicksInterval.containsKey(player.getUniqueId().toString())) {
                Storage.clicksInterval.put(player.getUniqueId().toString(), System.currentTimeMillis() + (10 * 1000));
            }
        }
    }
}
