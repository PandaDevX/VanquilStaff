package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.reports.Close;
import com.vanquil.staff.gui.inventory.reports.Open;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HomeListener implements Listener {

    public HomeListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e);

        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }
        if(!clickEvent.getTitle().startsWith("Main Menu")) {
            clickEvent = null;
            return;
        }

        String playerName = clickEvent.getTitle().split(" ")[3];
        if(clickEvent.clicked("Open Reports")) {

            e.setCancelled(true);
            Open open = new Open();
            open.setup(1, Bukkit.getOfflinePlayer(playerName));
            open.openInventory(clickEvent.getPlayer());

            open = null;
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked("Closed Reports")) {

            e.setCancelled(true);
            Close close = new Close();
            close.setup(1, Bukkit.getOfflinePlayer(playerName));
            close.openInventory(clickEvent.getPlayer());

            clickEvent = null;
            close = null;
            return;
        }


        e.setCancelled(true);
        clickEvent = null;
    }
}
