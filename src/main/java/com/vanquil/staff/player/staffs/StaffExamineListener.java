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
import org.bukkit.inventory.PlayerInventory;

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

        if(!clickEvent.getTitle().startsWith("(")) {
            clickEvent = null;
            return;
        }

        Player viewer = (Player) e.getWhoClicked();
        OfflinePlayer player = Bukkit.getOfflinePlayer(clickEvent.getTitle().split(" ")[1]);
        if(clickEvent.clicked("inventory")) {
            e.setCancelled(true);

            inventory = copyPlayerInventory(player.getPlayer().getInventory());
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
                try {
                    player.getInventory().setStorageContents(contents);
                    player.getInventory().setExtraContents(shield);
                }catch (NoSuchMethodError ex) {
                    player.getInventory().setContents(contents);
                }
                player.getInventory().setArmorContents(armor);

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

    public Inventory copyPlayerInventory(PlayerInventory inventory) {
        Inventory inv = Bukkit.createInventory(inventory.getHolder(), (9*5), inventory.getHolder().getName());
        if(!emptyStorage(inventory)) {
            for(int i = 0; i < 36; i++) {
                if(getContents(inventory)[i] == null)
                    continue;
                inv.setItem(i, getContents(inventory)[i]);
            }
        }
        if(!emptyArmor(inventory)) {
            for(int i = 36; i < inventory.getArmorContents().length; i++) {
                inv.setItem(i, inventory.getArmorContents()[i-36]);
            }
        }
        if(!emptyContents(inventory)) {
            inv.setItem(40, inventory.getExtraContents()[0]);
        }
        return inv;
    }

    private boolean emptyStorage(PlayerInventory inventory) {
        try {
            for(ItemStack item : inventory.getStorageContents())
            {
                if(item != null)
                    return false;
            }
        }catch (NoSuchMethodError e)  {
            for(ItemStack item : inventory.getContents())
            {
                if(item != null)
                    return false;
            }
        }
        return true;
    }

    private ItemStack[] getContents(PlayerInventory playerInventory) {
        try {
            return playerInventory.getStorageContents();
        }catch (NoSuchMethodError e) {
            return playerInventory.getContents();
        }
    }

    private boolean emptyArmor(PlayerInventory inventory) {
        for(ItemStack item : inventory.getArmorContents())
        {
            if(item != null)
                return false;
        }
        return true;
    }

    private boolean emptyContents(PlayerInventory inventory) {
        try {
            for(ItemStack item : inventory.getExtraContents())
            {
                if(item != null)
                    return false;
            }
        }catch (NoSuchMethodError e) {
            return true;
        }
        return true;
    }
}
