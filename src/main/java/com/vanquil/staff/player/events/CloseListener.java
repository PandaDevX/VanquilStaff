package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.reports.Open;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CloseListener implements Listener {

    public CloseListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e, "Closed Cases");

        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked(" ") && Utility.stripColor(clickEvent.getClickedItem().getItemMeta().getDisplayName()).startsWith("Page")) {
            clickEvent = null;
            e.setCancelled(true);
            return;
        }

        if(clickEvent.clicked("Prev")) {
            int current = Integer.parseInt(Utility.stripColor(clickEvent.getClickedInventory().getItem(31).getItemMeta().getDisplayName()).split(" ")[2]);

            Open open = new Open();
            open.setup(--current, clickEvent.getPlayer());
            open.openInventory(clickEvent.getPlayer());

            open = null;
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked("Next")) {
            int current = Integer.parseInt(Utility.stripColor(clickEvent.getClickedInventory().getItem(31).getItemMeta().getDisplayName()).split(" ")[2]);

            Open open = new Open();
            open.setup(++current, clickEvent.getPlayer());
            open.openInventory(clickEvent.getPlayer());

            open = null;
            clickEvent = null;
            return;
        }
        clickEvent = null;
        e.setCancelled(true);
        return;
    }
}
