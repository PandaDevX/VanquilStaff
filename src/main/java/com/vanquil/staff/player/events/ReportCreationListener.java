package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.Report;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ReportCreationListener implements Listener {

    public ReportCreationListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onReport(InventoryClickEvent e) {

        ClickEvent clickEvent = new ClickEvent(e, "Report");

        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }

        if(!Storage.playerReport.containsKey(e.getWhoClicked().getUniqueId().toString())) {
            e.setCancelled(true);
            clickEvent.getPlayer().closeInventory();
            clickEvent.getPlayer().sendTitle(Utility.colorize("&6&lReport"), Utility.colorize("&cTimed out"), 10, 70, 20);
            return;
        }
        if(clickEvent.clicked("submit")) {

            if(Storage.playerReport.containsKey(e.getWhoClicked().getUniqueId().toString())) {
                Storage.playerReport.get(e.getWhoClicked().getUniqueId().toString()).save();
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();

                clickEvent.getPlayer().sendTitle(Utility.colorize("&6&lReport"), Utility.colorize("&aSuccessfully sent the report"), 10, 70, 20);
                Storage.playerReportCoolDown.put(clickEvent.getPlayer().getUniqueId().toString(), (System.currentTimeMillis() + ((60 * 2) * 1000)));

                new BukkitRunnable() {
                    public void run() {
                        Storage.playerReportCoolDown.remove(e.getWhoClicked().getUniqueId().toString());
                    }
                }.runTaskLater(Staff.getInstance(), ((60L * 2L) * 20L));

                Storage.playerReport.remove(clickEvent.getPlayer().getUniqueId().toString());
                clickEvent = null;


                return;
            }
            clickEvent = null;
            e.setCancelled(true);
            return;
        }

        if(clickEvent.clicked("url")) {
            clickEvent.getPlayer().closeInventory();
            clickEvent.getPlayer().sendTitle(Utility.colorize("&6&lReport"), Utility.colorize("&7Enter supporting url or &acancel &7to cancel"), 10, 70, 20);
            Storage.playerReportEditing.put(clickEvent.getPlayer().getUniqueId().toString(), "url");
            e.setCancelled(true);
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked("reason")) {
            clickEvent.getPlayer().closeInventory();
            clickEvent.getPlayer().sendTitle(Utility.colorize("&6&lReason"), Utility.colorize("&7Enter reason or &acancel &7to cancel"), 10, 70, 20);
            Storage.playerReportEditing.put(clickEvent.getPlayer().getUniqueId().toString(), "reason");
            e.setCancelled(true);
            clickEvent = null;
            return;
        }

        clickEvent = null;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEditReport(AsyncPlayerChatEvent e) {
        if(!Storage.playerReportEditing.containsKey(e.getPlayer().getUniqueId().toString())) return;

        String category = Storage.playerReportEditing.get(e.getPlayer().getUniqueId().toString());
        Report report = new Report();
        com.vanquil.staff.database.Report playerReport = Storage.playerReport.get(e.getPlayer().getUniqueId().toString());
        switch (category) {
            case "url":
                if(e.getMessage().equalsIgnoreCase("cancel")) {
                    e.setCancelled(true);

                    report.setup(Storage.playerReport.get(e.getPlayer().getUniqueId().toString()));
                    report.openInventory(e.getPlayer());
                    report = null;
                    break;
                }
                e.setCancelled(true);
                playerReport.setURL(e.getMessage());
                Storage.playerReport.put(e.getPlayer().getUniqueId().toString(), playerReport);
                report.setup(Storage.playerReport.get(e.getPlayer().getUniqueId().toString()));
                report.openInventory(e.getPlayer());
                report = null;
                break;
            default:
                if(e.getMessage().equalsIgnoreCase("cancel")) {
                    e.setCancelled(true);
                    report.setup(Storage.playerReport.get(e.getPlayer().getUniqueId().toString()));
                    report.openInventory(e.getPlayer());
                    report = null;
                    break;
                }
                e.setCancelled(true);
                playerReport.setReason(e.getMessage());
                Storage.playerReport.put(e.getPlayer().getUniqueId().toString(), playerReport);
                report.setup(Storage.playerReport.get(e.getPlayer().getUniqueId().toString()));
                report.openInventory(e.getPlayer());
                report = null;
                break;
        }
        category = null;
    }

    @EventHandler
    public void onDelete(InventoryCloseEvent e) {
        if(!Storage.playerReport.containsKey(e.getPlayer().getUniqueId().toString()))return;
        new BukkitRunnable() {
            public void run() {
                Storage.playerReport.remove(e.getPlayer().getUniqueId().toString());
                Storage.playerReportEditing.remove(e.getPlayer().getUniqueId().toString());
            }
        }.runTaskLater(Staff.getInstance(), (60L * 20L));
    }
}
