package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StaffExamineListener implements Listener {

    Inventory inventory = null;
    Inventory eChest = null;

    public StaffExamineListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e);

        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }

        if(!clickEvent.getTitle().startsWith("Examine")) {
            clickEvent = null;
            return;
        }

        Player viewer = (Player) e.getWhoClicked();
        OfflinePlayer player = Bukkit.getOfflinePlayer(clickEvent.getTitle().split(" ")[2]);
        if(clickEvent.clicked("inventory")) {
            e.setCancelled(true);

            inventory = Bukkit.createInventory(player.getPlayer(), (9 * 5), Utility.colorize("&4" + player.getName() + "'s Inventory"));
            inventory.setContents(player.getPlayer().getInventory().getContents());
            viewer.openInventory(inventory);

            viewer = null;
            player = null;
            clickEvent = null;
            return;
        }
        if(clickEvent.clicked("ender chest")) {
            e.setCancelled(true);
            eChest = Bukkit.createInventory(player.getPlayer(), (9 * 3), Utility.colorize("&4" + player.getName() + "'s Ender Chest"));
            eChest.setContents(player.getPlayer().getEnderChest().getContents());
            viewer.openInventory(eChest);

            viewer = null;
            player = null;
            clickEvent = null;
            return;
        }

        viewer = null;
        player = null;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!e.getPlayer().hasPermission("staff.adminmode")) return;
        if(e.getInventory().equals(inventory)) {
            if(e.getInventory().getHolder() instanceof Player) {
                Player player = (Player) e.getInventory().getHolder();
                ItemStack[] contents =  new ItemStack[36];
                ItemStack[] armor = new ItemStack[4];
                ItemStack[] shield = {inventory.getItem(40) != null ? inventory.getItem(40) : new ItemStack(Material.AIR)};
                for(int i = 0; i < 36; i++) {
                    contents[i] = inventory.getItem(i) != null ? inventory.getItem(i) : new ItemStack(Material.AIR);
                }
                for(int i = 36; i <=39; i++) {
                    armor[i-36] = inventory.getItem(i) != null ? inventory.getItem(i) : new ItemStack(Material.AIR);
                }
                player.getInventory().setContents(contents);
                player.getInventory().setArmorContents(armor);
                player.getInventory().setExtraContents(shield);
                player.updateInventory();

                contents = null;
                armor = null;
                shield = null;
                player = null;
            }
        }

        if(e.getInventory().equals(eChest)) {
            if(e.getInventory().getHolder() instanceof Player) {
                Player player = (Player) e.getInventory().getHolder();
                ItemStack[] contents = new ItemStack[27];
                for(int i = 0; i < eChest.getSize(); i++) {
                    contents[i] = eChest.getItem(i) != null ? eChest.getItem(i) : new ItemStack(Material.AIR);
                }
                player.getEnderChest().setContents(contents);

                player = null;
                contents = null;
            }
        }
    }
}
