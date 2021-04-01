package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener {

    private Staff plugin;

    public DeathListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        // check if player's inventory is empty
        if(e.getEntity().getInventory().isEmpty()) return;

        // save player inventory
        Utility.saveInventory(e.getEntity());

        e.getDrops().clear();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        // check if player saved inventory
        if(!Storage.playerInventory.containsKey(e.getPlayer().getUniqueId().toString())) return;

        // alert probability to revert the inventory
        e.getPlayer().sendMessage(Utility.colorize(plugin.getConfig().getString("Restore Inventory.message")));
    }
}
