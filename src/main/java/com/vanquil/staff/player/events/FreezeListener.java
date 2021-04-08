package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {

    public FreezeListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        // check if player is frozen
        if(!Storage.frozenPlayers.contains(e.getPlayer().getUniqueId().toString())) return;

        // cancel movement if frozen
        if (e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
            if(e.getPlayer().isFlying()) {
                e.getPlayer().setFlying(false);
            }
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            e.setTo(e.getFrom());
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        // check if player is frozen
        if(!Storage.frozenPlayers.contains(e.getPlayer().getUniqueId().toString())) return;

        // cancel movement if frozen
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        // check if player is frozen
        if(!Storage.frozenPlayers.contains(e.getPlayer().getUniqueId().toString())) return;

        // cancel movement if frozen
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        // check if player is frozen
        if(!Storage.frozenPlayers.contains(e.getPlayer().getUniqueId().toString())) return;

        // cancel movement if frozen
        e.setCancelled(true);
    }
}
