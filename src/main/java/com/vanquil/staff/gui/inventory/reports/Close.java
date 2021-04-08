package com.vanquil.staff.gui.inventory.reports;

import com.vanquil.staff.database.Report;
import com.vanquil.staff.database.ReportDatabase;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import com.vanquil.staff.utility.PaginatedList;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Close {

    Inventory inventory = null;

    public void setup(int page, OfflinePlayer player) {

        InventoryBuilder builder = new InventoryBuilder("&4Closed Cases", 4);

        ReportDatabase reportDatabase = new ReportDatabase();
        List<Report> closeReports = new ArrayList<>();

        for(Report report : reportDatabase.getReports(player)) {

            if(report.isOpen())
                continue;

            closeReports.add(report);
        }
        if(closeReports.isEmpty()) {
            inventory = null;
            return;
        }

        PaginatedList paginatedList = new PaginatedList(closeReports, 27);
        List<Report> availableReports = paginatedList.getPage(page);

        for(int i = 0; i < availableReports.size(); i++) {

            ItemBuilder report = new ItemBuilder(availableReports.get(i).getPlayerHead());
            report.setName("&6" + availableReports.get(i).getReportedPlayer().getName());
            report.setLore("", "&2Reported by: &6" + availableReports.get(i).getReporter().getName(), "&2Date Closed: &6" + availableReports.get(i).getDate(),
                    "&2Reason: &6" + availableReports.get(i).getReport());

            builder.setItem(i + 1, report);

            report = null;
        }

        builder.placePlaceHolders(28, 36);

        ItemBuilder prev = new ItemBuilder(Material.MAP);
        prev.setName("&3Prev");

        ItemBuilder next = new ItemBuilder(Material.MAP);
        next.setName("&3Next");

        builder.setItem(34, next);
        builder.setItem(30, prev);

        ItemBuilder pageIdentifier = new ItemBuilder(Material.DROPPER);
        pageIdentifier.setName("&6Page &8(&c " + page + " / " + paginatedList.getMaximumPage() + " &8)");

        builder.setItem(32, pageIdentifier);

        inventory = builder.build();

        builder = null;
        reportDatabase = null;
        closeReports = null;
        paginatedList = null;
        availableReports = null;
        prev = null;
        next = null;
        pageIdentifier = null;

    }

    public void openInventory(Player player) {
        if(inventory == null) {
            player.sendMessage(Utility.colorize("&c&lReports>> &6There are no closed reports available"));
            return;
        }
        player.openInventory(inventory);
    }
}
