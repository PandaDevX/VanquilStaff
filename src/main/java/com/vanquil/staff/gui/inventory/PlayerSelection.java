package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import com.vanquil.staff.utility.PaginatedList;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;

public class PlayerSelection {

    Inventory inventory = null;

    public void setup(int page) {

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        PaginatedList paginatedList = new PaginatedList(players, 27);
        List<Player> availablePlayers = paginatedList.getPage(page);

        InventoryBuilder builder = new InventoryBuilder("&4Player Selection", 4);

        for(int i = 0; i < availablePlayers.size(); i++) {

            ItemBuilder itemBuilder = new ItemBuilder(Utility.getSkull(availablePlayers.get(i), "&c" + availablePlayers.get(i).getDisplayName()));
            builder.setItem(i + 1, itemBuilder);

            itemBuilder = null;
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
        prev = null;
        next = null;
        pageIdentifier = null;
        paginatedList = null;
        players = null;
        availablePlayers = null;
        builder = null;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
