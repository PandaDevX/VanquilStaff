package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class AdminMode {

    Inventory inventory = null;

    public void setup() {
        InventoryBuilder builder = new InventoryBuilder("&4Admin Mode", 1);


        ItemBuilder randomTP = new ItemBuilder(Material.HOPPER);
        randomTP.setName("&6Random TP");

        ItemBuilder examine = new ItemBuilder(Material.CHEST);
        examine.setName("&6Examine");

        ItemBuilder freeze = new ItemBuilder(Material.ICE);
        freeze.setName("&6Freeze Tool");

        ItemBuilder follow = new ItemBuilder(Material.GOLD_INGOT);
        follow.setName("&6Follow Tool");

        ItemBuilder push = new ItemBuilder(Material.TNT);
        push.setName("&6Push Forward");

        ItemBuilder staffs = new ItemBuilder(Material.DISPENSER);
        staffs.setName("&6Staffs");

        ItemBuilder we = new ItemBuilder(Material.STONE_AXE);
        we.setName("&6WE Wand");
        we.setFlag(ItemFlag.HIDE_ATTRIBUTES);

        builder.setItem(1, randomTP);
        builder.setItem(2, examine);
        builder.setItem(3, freeze);
        builder.setItem(4, follow);
        builder.setItem(5, push);
        builder.setItem(6, staffs);
        builder.setItem(7, we);

        inventory = builder.build();

        builder = null;
        randomTP = null;
        examine = null;
        freeze = null;
        follow = null;
        staffs = null;
        push = null;
        we = null;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
