package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DeathListener implements Listener {

    private Staff plugin;

    public DeathListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        // check if player's inventory is empty
        if(!emptyInventory(e.getEntity().getInventory())) {

            // save player inventory
            try {
                Storage.playerInventory.put(e.getEntity().getUniqueId().toString(), e.getEntity().getInventory().getStorageContents());
            }catch (NoSuchMethodError ex) {
                Storage.playerInventory.put(e.getEntity().getUniqueId().toString(), e.getEntity().getInventory().getContents());
            }
        }

        if(!emptyArmors(e.getEntity().getInventory())) {
            Storage.playerArmors.put(e.getEntity().getUniqueId().toString(), e.getEntity().getInventory().getArmorContents());
        }


        if(!isOldVersion(e.getEntity().getInventory())) {
            Storage.playerExtras.put(e.getEntity().getUniqueId().toString(), e.getEntity().getInventory().getExtraContents());
        }

        e.getDrops().clear();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        // check if player saved inventory
        if(!Storage.playerInventory.containsKey(e.getPlayer().getUniqueId().toString())
        && !Storage.playerArmors.containsKey(e.getPlayer().getUniqueId().toString())) return;

        if(!isOldVersion(e.getPlayer().getInventory())) {
            if(!Storage.playerExtras.containsKey(e.getPlayer().getUniqueId().toString())) return;
        }

        // alert probability to revert the inventory
        e.getPlayer().sendMessage(Utility.colorize(plugin.getConfig().getString("Restore Inventory.message")));

    }

    public boolean emptyInventory(PlayerInventory inventory) {
        for(ItemStack item : inventory.getContents())
        {
            if(item != null)
                return false;
        }
        return true;
    }

    public boolean emptyArmors(PlayerInventory inventory) {
        for(ItemStack item : inventory.getArmorContents()) {
            if(item != null)
                return false;
        }
        return true;
    }

    public boolean isOldVersion(PlayerInventory inventory) {
        try {
            inventory.getExtraContents();
        }catch (NoSuchMethodError e) {
            return true;
        }
        return false;
    }
}
