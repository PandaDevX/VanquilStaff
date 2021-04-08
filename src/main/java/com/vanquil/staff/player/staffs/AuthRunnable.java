package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.PinDatabase;
import com.vanquil.staff.gui.inventory.Pin;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AuthRunnable implements Runnable {
    private InventoryClickEvent e;
    public AuthRunnable(InventoryClickEvent e) {
        this.e = e;
    }
    @Override
    public void run() {
        if(!Utility.stripColor(e.getView().getTitle()).equals("Pin")) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory() == e.getWhoClicked().getInventory()) {
            e.setCancelled(true);
            return;
        }
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")
                || e.getCurrentItem().getItemMeta().getDisplayName().endsWith("Wrong Password")) {
            e.setCancelled(true);
            return;
        }

        if(Utility.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Register")) {
            e.setCancelled(true);

            // save
            PinDatabase pinDatabase = new PinDatabase((Player) e.getWhoClicked());
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < e.getWhoClicked().getInventory().getContents().length; i++) {
                if(e.getWhoClicked().getInventory().getItem(i) == null || e.getWhoClicked().getInventory().getItem(i).equals(new ItemStack(Material.AIR)))
                    continue;
                if(!e.getWhoClicked().getInventory().getItem(i).hasItemMeta())
                    continue;
                if(!e.getWhoClicked().getInventory().getItem(i).getItemMeta().hasDisplayName())
                    continue;
                builder.append(Utility.stripColor(e.getWhoClicked().getInventory().getItem(i).getItemMeta().getDisplayName()));
            }

            pinDatabase.register(builder.toString());

            if(!Staff.getInstance().getConfig().getBoolean("Auth Pin.login_after_register")) {
                Pin pin = new Pin();
                pin.setup("login");
                pin.openInventory((Player) e.getWhoClicked());
            }
            e.getWhoClicked().closeInventory();
            return;
        }

        if(Utility.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Login")) {
            e.setCancelled(true);

            // save
            PinDatabase pinDatabase = new PinDatabase((Player) e.getWhoClicked());
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < e.getWhoClicked().getInventory().getContents().length; i++) {
                if(e.getWhoClicked().getInventory().getItem(i) == null || e.getWhoClicked().getInventory().getItem(i).equals(new ItemStack(Material.AIR)))
                    continue;
                if(!e.getWhoClicked().getInventory().getItem(i).hasItemMeta())
                    continue;
                if(!e.getWhoClicked().getInventory().getItem(i).getItemMeta().hasDisplayName())
                    continue;
                builder.append(Utility.stripColor(e.getWhoClicked().getInventory().getItem(i).getItemMeta().getDisplayName()));
            }

            if(pinDatabase.login(builder.toString())) {
                e.getWhoClicked().closeInventory();
                Storage.staffAttempt.remove(e.getWhoClicked().getUniqueId().toString());
                return;
            }
            int current = Storage.staffAttempt.getOrDefault(e.getWhoClicked().getUniqueId().toString(), 0);

            if(current == (Staff.getInstance().getConfig().getInt("Auth Pin.max_attempt") - 1)) {
                ((Player) e.getWhoClicked()).kickPlayer(Utility.colorize("&c&lHey &fYou reached the maximum amount of attempts"));
                Storage.staffAttempt.remove(e.getWhoClicked().getUniqueId().toString());
                Storage.playerIndexPin.remove(e.getWhoClicked().getUniqueId().toString());

                if(Storage.staffInventory.containsKey(e.getWhoClicked().getUniqueId().toString())) {
                    e.getWhoClicked().getInventory().setContents(Storage.staffInventory.get(e.getWhoClicked().getUniqueId().toString()));
                    Storage.staffInventory.remove(e.getWhoClicked().getUniqueId().toString());
                }
                ((Player) e.getWhoClicked()).sendTitle(Utility.colorize("&6Auth Pin"), Utility.colorize("&aSuccessfully logged in"), 10, 70, 20);

                Storage.playerIndexPin.remove(e.getWhoClicked().getUniqueId().toString());
                return;
            }
            Storage.staffAttempt.put(e.getWhoClicked().getUniqueId().toString(), ++current);
            Utility.showWrongInfo(e.getClickedInventory(), e.getSlot());
            return;
        }

        if(Utility.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Eraser")) {
            if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                e.getWhoClicked().getInventory().clear();
                e.setCancelled(true);
                Storage.playerIndexPin.remove(e.getWhoClicked().getUniqueId().toString());
                return;
            }
            e.setCancelled(true);
            int index = Storage.playerIndexPin.getOrDefault(e.getWhoClicked().getUniqueId().toString(), 0);
            e.setCancelled(true);
            if(index <= 0) {
                Storage.playerIndexPin.put(e.getWhoClicked().getUniqueId().toString(), 0);
                return;
            }

            e.getWhoClicked().getInventory().setItem(index - 1,null);
            ((Player)e.getWhoClicked()).updateInventory();
            Storage.playerIndexPin.put(e.getWhoClicked().getUniqueId().toString(), index - 1);
            return;
        }

        e.setCancelled(true);
        int index = Storage.playerIndexPin.getOrDefault(e.getWhoClicked().getUniqueId().toString(), 0);
        e.getWhoClicked().getInventory().setItem(index,e.getCurrentItem());
        ((Player)e.getWhoClicked()).updateInventory();
        Storage.playerIndexPin.put(e.getWhoClicked().getUniqueId().toString(), index + 1);
    }
}
