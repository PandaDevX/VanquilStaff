package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.DatabaseManager;
import com.vanquil.staff.database.PinDatabase;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.Pin;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class StaffAuth implements Listener {

    public StaffAuth(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent e) {
        if(DatabaseManager.getConnection() == null) return;
        if(!Utility.getStaffNames().contains(e.getPlayer().getName())) return;

        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;
        Utility.auth(e.getPlayer());
        pinDatabase = null;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(DatabaseManager.getConnection() == null) return;

        PinDatabase pinDatabase = new PinDatabase((Player) e.getPlayer());
        final String type = pinDatabase.isRegistered() ? "login" : "register";
        if(!e.getView().getTitle().endsWith("Pin")) return;

        if(pinDatabase.isLoggedIn()) {
            Utility.restoreInventory((Player) e.getPlayer());
            e.getPlayer().sendMessage(Utility.colorize("&a&lVanquil &8>> &aSuccessfully logged in"));

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
    public void onClick(final InventoryClickEvent e) {
        if(e.isAsynchronous()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Staff.getInstance(), new AuthRunnable(e));
        } else {
            new AuthRunnable(e).run();
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(DatabaseManager.getConnection() == null) return;
        if(!Utility.getStaffNames().contains(e.getPlayer().getName())) return;
        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());
        if(pinDatabase.isLoggedIn()) return;
        pinDatabase = null;
        if(e.getFrom().getX() != e.getTo().getX() && e.getFrom().getZ() != e.getTo().getZ()) {
            e.setTo(e.getFrom());
        }
    }

}
