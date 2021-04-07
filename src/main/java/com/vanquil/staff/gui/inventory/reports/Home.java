package com.vanquil.staff.gui.inventory.reports;

import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class Home {

    Inventory inventory = null;

    public void setup() {

        InventoryBuilder builder = new InventoryBuilder("&4Main Menu", InventoryType.HOPPER);

        builder.placePlaceHolders(1, 5, Material.BLACK_STAINED_GLASS_PANE);

        ItemBuilder open = new ItemBuilder(Material.SPAWNER);
        open.setName("&6Open Reports");

        ItemBuilder close = new ItemBuilder(Material.LEATHER_CHESTPLATE);
        close.setName("&6Closed Reports");

        builder.setItem(2, open);
        builder.setItem(4, close);

        builder = null;
        open = null;
        close = null;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
