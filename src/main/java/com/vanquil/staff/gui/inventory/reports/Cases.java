package com.vanquil.staff.gui.inventory.reports;

import com.vanquil.staff.database.ReportDatabase;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import com.vanquil.staff.utility.PaginatedList;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cases {

    Inventory inventory = null;

    public void setup(int page) {

        InventoryBuilder builder = new InventoryBuilder("&4Cases", 4);

        ReportDatabase reportDatabase = new ReportDatabase();

        List<String> reportedPlayers = new ArrayList<>(reportDatabase.getReportedPlayers());

        PaginatedList paginatedList = new PaginatedList(reportedPlayers, 27);
        if(page > paginatedList.getMaximumPage()) {
            page = paginatedList.getMaximumPage();
        }
        if(page <= 0) {
            page = 1;
        }
        List<String> availableReports = paginatedList.getPage(page);

        if(availableReports.isEmpty()) {
            inventory = null;
            return;
        }

        for(int i = 0; i < availableReports.size(); i++) {

            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(availableReports.get(i)));
            ItemBuilder report = new ItemBuilder(Utility.getSkull(player, "&6" + player.getName()));
            report.setLore("", "&2Number Of Open Cases: &6" + reportDatabase.getNumberOfOpenReports(player) + " Report(s)",
                    "&2Number Of Closed Cases: &6: &6" + (reportDatabase.getNumberOfReports(player) - reportDatabase.getNumberOfOpenReports(player)) + " Report(s)",
                    "&2Latest Report Date: &6" + Utility.getLatestReport(reportDatabase.getReports(player)).getDate(),
                    "&2Person Reported This Player (Latest): &6" + Utility.getLatestReport(reportDatabase.getReports(player)).getReporter().getName());

            builder.setItem(i + 1, report);
            report = null;
        }

        builder.placePlaceHolders(28, 36, Material.BLACK_STAINED_GLASS_PANE);

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
        reportedPlayers = null;
        paginatedList = null;
        availableReports = null;
        prev = null;
        next = null;
        pageIdentifier = null;

    }

    public void openInventory(Player player) {
        if(inventory == null) {
            player.sendMessage(Utility.colorize("&c&lReports>> &6There are no open reports available"));
            return;
        }
        player.openInventory(inventory);
    }
}
