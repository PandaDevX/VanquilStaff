package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {

    public FreezeListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        // check if player is frozen
        if(!Utility.isFrozen(e.getPlayer())) return;

        // cancel movement if frozen
        if (e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
            if(e.getPlayer().isFlying()) {
                e.getPlayer().setFlying(false);
            }
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            e.setTo(e.getFrom());
            e.getPlayer().sendMessage(Utility.colorize("&a&lVanquil &8>> &cYou can not do that while frozen"));
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        // check if player is frozen
        if(!Utility.isFrozen(e.getPlayer())) return;

        // cancel movement if frozen
        e.setCancelled(true);
        e.getPlayer().sendMessage(Utility.colorize("&a&lVanquil &8>> &cYou can not do that while frozen"));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        // check if player is frozen
        if(!Utility.isFrozen(e.getPlayer())) return;

        // cancel movement if frozen
        e.setCancelled(true);
        e.getPlayer().sendMessage(Utility.colorize("&a&lVanquil &8>> &cYou can not do that while frozen"));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        // check if player is frozen
        if(!Utility.isFrozen(e.getPlayer())) return;

        // cancel movement if frozen
        e.setCancelled(true);
        e.getPlayer().sendMessage(Utility.colorize("&a&lVanquil &8>> &cYou can not do that while frozen"));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        if(!Utility.isFrozen(e.getPlayer())) return;

        e.setCancelled(true);

        e.getPlayer().sendMessage(Utility.colorize("&a&lVanquil &8>> &cYou can not do that while frozen"));
    }
}
