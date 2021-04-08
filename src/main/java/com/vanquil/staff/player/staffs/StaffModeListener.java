package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.PlayerSelection;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StaffModeListener implements Listener {

    public StaffModeListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onStaffMode(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e, "Staff Mode");
        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked(" ")) {
            e.setCancelled(true);
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked("Random TP")) {
            e.setCancelled(true);
            clickEvent.getPlayer().closeInventory();
            Utility.randomizeTeleport(clickEvent.getPlayer(), clickEvent.getPlayer().getWorld());
            clickEvent = null;
            return;
        }

        if(clickEvent.clicked("Staffs")) {
            e.setCancelled(true);

            StaffGuiOnline staffGuiOnline = new StaffGuiOnline();
            staffGuiOnline.setup();
            staffGuiOnline.openInventory(clickEvent.getPlayer());

            staffGuiOnline = null;
            clickEvent = null;
            return;
        }

        String name = Utility.stripColor(clickEvent.getClickedItem().getItemMeta().getDisplayName());

        Storage.staffTool.put(clickEvent.getPlayer().getUniqueId().toString(), name);

        e.setCancelled(true);
        PlayerSelection playerSelection = new PlayerSelection();
        playerSelection.setup(1);
        playerSelection.openInventory(clickEvent.getPlayer());

        name = null;
        playerSelection = null;
        clickEvent = null;
    }
}
