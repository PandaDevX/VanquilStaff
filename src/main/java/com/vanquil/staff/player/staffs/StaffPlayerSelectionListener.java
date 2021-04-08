package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.gui.events.ClickEvent;
import com.vanquil.staff.gui.inventory.Examine;
import com.vanquil.staff.gui.inventory.PlayerSelection;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StaffPlayerSelectionListener implements Listener {

    public StaffPlayerSelectionListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ClickEvent clickEvent = new ClickEvent(e, "Player Selection");

        if(!clickEvent.passed()) {
            clickEvent = null;
            return;
        }
        if(clickEvent.clicked(" ") || Utility.stripColor(clickEvent.getClickedItem().getItemMeta().getDisplayName()).startsWith("Page")) {
            e.setCancelled(true);
            clickEvent = null;
            return;
        }
        if(clickEvent.clicked("next")) {
            int current = Integer.parseInt(Utility.stripColor(clickEvent.getClickedInventory().getItem(31).getItemMeta().getDisplayName()).split(" ")[2]);
            PlayerSelection playerSelection = new PlayerSelection();
            playerSelection.setup(++current);
            e.setCancelled(true);
            playerSelection.openInventory(clickEvent.getPlayer());
            clickEvent = null;
            playerSelection = null;
            return;
        }
        if(clickEvent.clicked("prev")) {
            int current = Integer.parseInt(Utility.stripColor(clickEvent.getClickedInventory().getItem(31).getItemMeta().getDisplayName()).split(" ")[2]);
            PlayerSelection playerSelection = new PlayerSelection();
            playerSelection.setup(--current);
            e.setCancelled(true);
            playerSelection.openInventory(clickEvent.getPlayer());
            clickEvent = null;
            playerSelection = null;
            return;
        }

        e.setCancelled(true);

        String playerName = Utility.stripColor(clickEvent.getClickedItem().getItemMeta().getDisplayName());

        Storage.playerSelection.put(clickEvent.getPlayer().getUniqueId().toString(), playerName);

        action(Storage.staffTool.get(clickEvent.getPlayer().getUniqueId().toString()), clickEvent.getPlayer());

        playerName = null;
        clickEvent = null;
    }

    public void action(String tool, Player player) {
        switch (tool) {
            case "Examine":
                Examine examine = new Examine();
                examine.setup(Storage.playerSelection.get(player.getUniqueId().toString()));
                examine.openInventory(player);
                examine = null;
                break;
            case "Freeze Tool":
                Player target = Bukkit.getPlayer(Storage.playerSelection.get(player.getUniqueId().toString()));

                // if target is not online
                if(target == null) {

                    // send alert
                    player.sendMessage(Utility.colorize("&c&lHey &fthat player is offline"));
                    break;
                }

                // check if already frozen
                if(Storage.frozenPlayers.contains(target.getUniqueId().toString())) {

                    // unfreeze player
                    Storage.frozenPlayers.remove(target.getUniqueId().toString());

                    // send alert
                    target.sendMessage(Utility.colorize("&6&lFreeze &aYou are now able to move"));

                    player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &7Successfully removed frozen spell to that player"));
                    break;
                }

                // freeze self
                Storage.frozenPlayers.add(target.getUniqueId().toString());

                // send alert
                target.sendMessage(Utility.colorize("&6&lFreeze &aYou are no longer able to move"));

                player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &7Successfully added frozen spell to that player"));
                break;
            case "Follow Tool":
                Utility.followPlayer(player, Bukkit.getPlayer(Storage.playerSelection.get(player.getUniqueId().toString())));
                break;
            case "Push Forward":
                player.closeInventory();
                if(Utility.pushPlayer(Bukkit.getPlayer(Storage.playerSelection.get(player.getUniqueId().toString())))) {
                    player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &7Successfully pushed that player"));
                    break;
                }
                player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &7Player not found"));
                break;
            default:
                break;
        }
        Storage.staffTool.remove(player.getUniqueId().toString());
        Storage.playerInventory.remove(player.getUniqueId().toString());
    }
}
