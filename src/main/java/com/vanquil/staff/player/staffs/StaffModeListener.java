package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.gui.inventory.PlayerSelection;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class StaffModeListener implements Listener {

    public StaffModeListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onStaffMode(PlayerInteractEvent e) {

        if((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() == null) return;
            if (!e.getItem().hasItemMeta()) return;
            if (!Storage.staffMode.contains(e.getPlayer().getUniqueId().toString())) return;

            if (clicked(e, "Random TP")) {
                e.getPlayer().closeInventory();
                Utility.randomizeTeleport(e.getPlayer(), e.getPlayer().getWorld());

                e.setCancelled(true);
                return;
            }

            if (clicked(e, "Staffs")) {


                StaffGuiOnline staffGuiOnline = new StaffGuiOnline();
                staffGuiOnline.setup();
                staffGuiOnline.openInventory(e.getPlayer());


                e.setCancelled(true);
                staffGuiOnline = null;
                return;
            }

            String name = Utility.stripColor(e.getItem().getItemMeta().getDisplayName());

            Storage.staffTool.put(e.getPlayer().getUniqueId().toString(), name);

            PlayerSelection playerSelection = new PlayerSelection();
            playerSelection.setup(1);
            playerSelection.openInventory(e.getPlayer());

            e.setCancelled(true);

            name = null;
            playerSelection = null;
        }
    }

    @EventHandler
    public void onStaffModePlace(BlockPlaceEvent e) {
        if(!Storage.staffMode.contains(e.getPlayer().getUniqueId().toString())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onStaffModePlace(InventoryClickEvent e) {
        if(!Storage.staffMode.contains(e.getWhoClicked().getUniqueId().toString())) return;
        if(e.getClickedInventory() != null && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) return;
        e.setCancelled(true);
    }

    private boolean clicked(PlayerInteractEvent e, String name) {
        return Utility.stripColor(e.getItem().getItemMeta().getDisplayName()).equals(name);
    }

    @EventHandler
    public void onStaffModePlace(BlockBreakEvent e) {
        if(!Storage.staffMode.contains(e.getPlayer().getUniqueId().toString())) return;
        e.setCancelled(true);
    }
}
