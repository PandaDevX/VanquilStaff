package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Report {

    Inventory inventory = null;

    public void setup(com.vanquil.staff.database.Report report) {

        InventoryBuilder builder = new InventoryBuilder("&4Report", 3);

        builder.placePlaceHolders(1, 10, Material.LIGHT_BLUE_STAINED_GLASS);
        builder.placePlaceHolders(18, 27, Material.LIGHT_BLUE_STAINED_GLASS);

        ItemBuilder design1 = new ItemBuilder(Material.NETHER_STAR);
        builder.setItem(5, design1.createPlaceHolder());

        ItemBuilder design2 = new ItemBuilder(Material.GRAY_DYE);
        builder.setItem(21, design2.createPlaceHolder());
        builder.setItem(25, design2.createPlaceHolder());

        ItemBuilder design3 = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setItem(11, design3.createPlaceHolder());
        builder.setItem(13, design3.createPlaceHolder());
        builder.setItem(15, design3.createPlaceHolder());
        builder.setItem(17, design3.createPlaceHolder());

        ItemBuilder head = new ItemBuilder(Utility.getSkull(report.getReportedPlayer(), "&6" + report.getReportedPlayer().getName()));
        builder.setItem(12, head);

        ItemBuilder paper = new ItemBuilder(Material.PAPER);
        paper.setName("&eReason");
        paper.setLore("", "&r" + report.getReport());

        ItemBuilder url = new ItemBuilder(Material.HOPPER);
        url.setName("&eURL");
        paper.setLore("", "&5CLICK &6To add supporting link");

        ItemBuilder submit = new ItemBuilder(Material.DIAMOND);
        submit.setName("&aSubmit");
        builder.setItem(23, submit);

        inventory = builder.build();

        builder = null;
        design1 = null;
        design2 = null;
        design3 = null;
        head = null;
        paper = null;
        url = null;
        submit = null;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
