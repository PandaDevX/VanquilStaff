package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.reports.Cases;
import com.vanquil.staff.gui.inventory.reports.Home;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CasesListener implements Listener {

    public CasesListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e, "Cases");

        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked(" ") || Utility.stripColor(clickEvent.getClickedItem().getItemMeta().getDisplayName()).startsWith("Page")) {
            e.setCancelled(true);
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked("next")) {
            String name = clickEvent.getClickedInventory().getItem(31).getItemMeta().getDisplayName();
            int current = Integer.parseInt(Utility.stripColor(name).split(" ")[2]);
            Cases cases = new Cases();
            cases.setup(++current);
            cases.openInventory(clickEvent.getPlayer());

            clickEvent = null;
            cases = null;
            name = null;
            return;
        }

        if(clickEvent.clicked("prev")) {
            String name = clickEvent.getClickedInventory().getItem(31).getItemMeta().getDisplayName();
            int current = Integer.parseInt(Utility.stripColor(name).split(" ")[2]);
            Cases cases = new Cases();
            cases.setup(--current);
            cases.openInventory(clickEvent.getPlayer());

            clickEvent = null;
            cases = null;
            name = null;
            return;
        }

        String playerName = Utility.stripColor(clickEvent.getClickedItem().getItemMeta().getDisplayName());

        Home home = new Home();
        home.setup(playerName);
        home.openInventory(clickEvent.getPlayer());

        home = null;
        playerName = null;
        clickEvent = null;
    }
}
