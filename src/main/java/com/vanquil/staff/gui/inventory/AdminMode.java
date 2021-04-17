package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class AdminMode {

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

        ItemBuilder we = new ItemBuilder(Material.STONE_AXE);
        we.setName("&6WE Wand");
        we.setFlag(ItemFlag.HIDE_ATTRIBUTES);

        ItemBuilder reports = new ItemBuilder(Material.BOOK);
        reports.setName("&6Reports");
        reports.setEnchantment(Enchantment.FIRE_ASPECT, 1);
        reports.setFlag(ItemFlag.HIDE_ENCHANTS);

        ItemBuilder cps = new ItemBuilder(Material.BEDROCK);
        cps.setName("&6CPS Checker");

        player.getInventory().setItem(0, randomTP.build());
        player.getInventory().setItem(1, examine.build());
        player.getInventory().setItem(2, freeze.build());
        player.getInventory().setItem(3, follow.build());
        player.getInventory().setItem(4, push.build());
        player.getInventory().setItem(5, staffs.build());
        player.getInventory().setItem(6, we.build());
        player.getInventory().setItem(7, reports.build());
        player.getInventory().setItem(8, cps.build());

        player.updateInventory();


        randomTP = null;
        examine = null;
        freeze = null;
        follow = null;
        staffs = null;
        push = null;
        we = null;
    }
}
