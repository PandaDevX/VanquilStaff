package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class StaffMode {


    public void setup(Player player) {


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

        player.getInventory().setItem(0, randomTP.build());
        player.getInventory().setItem(1, examine.build());
        player.getInventory().setItem(2, freeze.build());
        player.getInventory().setItem(3, follow.build());
        player.getInventory().setItem(4, push.build());
        player.getInventory().setItem(5, staffs.build());

        randomTP = null;
        examine = null;
        freeze = null;
        follow = null;
        push = null;
    }
}
