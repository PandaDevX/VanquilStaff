package com.vanquil.staff.gui.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ClickEvent {

    private InventoryClickEvent e;
    private String name;
    private ItemStack clickedItem;
    public ClickEvent(InventoryClickEvent e, String name) {
        this.e = e;
        this.name = name;
        this.clickedItem = e.getCurrentItem();
    }

    public ClickEvent(InventoryClickEvent e) {
        this.e = e;
        this.name = null;
        this.clickedItem = e.getCurrentItem();
    }

    public boolean passed() {
        if(e.getClickedInventory() == null) return false;
        if(e.getCurrentItem() == null) return false;
        if(!e.getCurrentItem().hasItemMeta()) return false;
        if(name != null) {
            String stripTitle = Utility.stripColor(e.getView().getTitle());
            String stripName = Utility.stripColor(name);
            return stripTitle.equals(stripName);
        }
        return true;
    }

    public Player getPlayer() {
        return (Player) e.getWhoClicked();
    }

    public int getSlot() {
        return e.getSlot() + 1;
    }

    public InventoryBuilder getInventoryBuilder() {
        return new InventoryBuilder(e.getClickedInventory());
    }

    public boolean clicked(String name) {
        return Utility.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(name);
    }

    public ItemStack getClickedItem() {
        return e.getCurrentItem();
    }

    public Inventory getClickedInventory() {
        return e.getClickedInventory();
    }

    public String getTitle() {
        return Utility.stripColor(e.getView().getTitle());
    }

    public void showNoPermission() {
        final ItemStack clicked = clickedItem;
        ItemBuilder builder = new ItemBuilder(Material.RED_CONCRETE);
        builder.setName("&4Insufficient Permission");
        getInventoryBuilder().setItem(this.getSlot(), builder);

        new BukkitRunnable() {
            public void run() {
                getInventoryBuilder().setItem(getSlot(), clicked);
            }
        }.runTaskLater(Staff.getInstance(), 60L);
    }

    public void showInsufficientBalance() {
        final ItemStack clicked = clickedItem;
        ItemBuilder builder = new ItemBuilder(Material.RED_CONCRETE);
        builder.setName("&4Insufficient Balance");
        getInventoryBuilder().setItem(this.getSlot(), builder);

        new BukkitRunnable() {
            public void run() {
                getInventoryBuilder().setItem(getSlot(), clicked);
            }
        }.runTaskLater(Staff.getInstance(), 60L);
    }
}

