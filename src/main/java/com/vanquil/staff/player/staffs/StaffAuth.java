package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.PinDatabase;
import com.vanquil.staff.gui.inventory.Pin;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class StaffAuth implements Listener {

    public StaffAuth(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent e) {
        if(!Utility.getStaffNames().contains(e.getPlayer().getName())) return;

        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;
        Utility.auth(e.getPlayer());
        pinDatabase = null;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {


        PinDatabase pinDatabase = new PinDatabase((Player) e.getPlayer());
        final String type = pinDatabase.isRegistered() ? "login" : "register";
        if(!e.getView().getTitle().endsWith("Pin")) return;

        if(pinDatabase.isLoggedIn()) {
            if(Storage.staffInventory.containsKey(e.getPlayer().getUniqueId().toString())) {
                e.getPlayer().getInventory().setContents(Storage.staffInventory.get(e.getPlayer().getUniqueId().toString()));
                Storage.staffInventory.remove(e.getPlayer().getUniqueId().toString());
            }
            ((Player) e.getPlayer()).sendTitle(Utility.colorize("&6Auth Pin"), Utility.colorize("&aSuccessfully logged in"), 10, 70, 20);

            Storage.playerIndexPin.remove(e.getPlayer().getUniqueId().toString());
            return;
        }

        new BukkitRunnable() {
            public void run () {

                // open gui
                Pin pin = new Pin();
                pin.setup(type);
                pin.openInventory((Player) e.getPlayer());
                pin = null;
            }
        }.runTaskLater(Staff.getInstance(), 1);

        pinDatabase = null;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().endsWith("Pin")) return;

        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        // play sound
        ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_CHICKEN_EGG, 5, 5);

        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")
        || e.getCurrentItem().getItemMeta().getDisplayName().endsWith("Wrong Password")) {
            e.setCancelled(true);
            return;
        }

        if(e.getCurrentItem().getItemMeta().getDisplayName().endsWith("Register")) {
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

        if(e.getCurrentItem().getItemMeta().getDisplayName().endsWith("Login")) {
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

        if(e.getCurrentItem().getItemMeta().getDisplayName().endsWith("Eraser")) {
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
            Storage.playerIndexPin.put(e.getWhoClicked().getUniqueId().toString(), --index);
            e.getWhoClicked().getInventory().setItem(Storage.playerIndexPin.get(e.getWhoClicked().getUniqueId().toString()), null);

            return;
        }

        e.setCancelled(true);
        int index = Storage.playerIndexPin.getOrDefault(e.getWhoClicked().getUniqueId().toString(), 0);
        e.getWhoClicked().getInventory().setItem(index, e.getCurrentItem());
        Storage.playerIndexPin.put(e.getWhoClicked().getUniqueId().toString(), ++index);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;

        e.setCancelled(true);
        pinDatabase = null;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;

        e.setCancelled(true);
        pinDatabase = null;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;

        e.setCancelled(true);
        pinDatabase = null;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;

        e.setCancelled(true);
        pinDatabase = null;
    }

}
