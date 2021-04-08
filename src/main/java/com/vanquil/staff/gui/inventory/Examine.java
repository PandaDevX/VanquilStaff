package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class Examine {

    private Inventory inventory = null;

    public void setup(String name) {
        InventoryBuilder builder = new InventoryBuilder("&4Examine &8( &c" + name + " &8)", InventoryType.HOPPER);

        builder.placePlaceHolders(1, 5, Material.BLACK_STAINED_GLASS_PANE);

        ItemBuilder chest = new ItemBuilder(Material.CHEST);

        chest.setName("&6Inventory");

        builder.setItem(2, chest);

        ItemBuilder echest = new ItemBuilder(Material.ENDER_CHEST);

        echest.setName("&6Ender Chest");
        builder.setItem(4, echest);

        inventory = builder.build();
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
