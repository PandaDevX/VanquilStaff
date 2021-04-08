package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.database.ReportDatabase;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.reports.Open;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OpenListener implements Listener {

    public OpenListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e, "Open Cases");

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
        if(e.getClick() == ClickType.RIGHT) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(Utility.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
            String reason = Utility.stripColor(e.getCurrentItem().getItemMeta().getLore().get(3)).replace("Reason: ","");
            String date = Utility.stripColor(e.getCurrentItem().getItemMeta().getLore().get(2)).replace("Date Reported: ", "");
            ReportDatabase reportDatabase = new ReportDatabase();
            reportDatabase.close(player, reason,date);


            e.getWhoClicked().sendMessage(reason);
            e.getWhoClicked().sendMessage(date);

            e.setCancelled(true);

            e.getClickedInventory().setItem(e.getSlot(), null);
            date = null;
            reason = null;
            player = null;
            reportDatabase = null;
            return;
        }
        e.setCancelled(true);
        return;
    }
}
