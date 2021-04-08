package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StaffsListener implements Listener {

    public StaffsListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        ClickEvent clickEvent = new ClickEvent(e);
        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }
        if(!clickEvent.getTitle().endsWith("Staffs")) {
            clickEvent = null;
            return;
        }
        e.setCancelled(true);
        clickEvent = null;
        return;
    }
}
