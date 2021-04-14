package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class ReviveCommand implements CommandExecutor {

    private Staff plugin;
    public ReviveCommand(Staff plugin) {
        plugin.getCommand("revive").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check if player
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &fyou must be a player to do that"));
            return true;
        }
        // check permission

        if(!sender.hasPermission("player.revive")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        // check if has saved inventory
        Player player = (Player) sender;
        if(!Storage.playerInventory.containsKey(player.getUniqueId().toString()) && !Storage.playerExtras.containsKey(player.getUniqueId().toString()) && !Storage.playerArmors.containsKey(player.getUniqueId().toString())) {
            player.sendMessage(Utility.colorize(plugin.getConfig().getString("Restore Inventory.unavailable")));
            return true;
        }

        // revive inventory
        if(Storage.playerInventory.containsKey(player.getUniqueId().toString())) {
            try {
                player.getInventory().setStorageContents(Storage.playerInventory.get(player.getUniqueId().toString()));
            }catch (NoSuchMethodError e) {
                player.getInventory().setContents(Storage.playerInventory.get(player.getUniqueId().toString()));
            }
            Storage.playerInventory.remove(player.getUniqueId().toString());
        }
        if(Storage.playerArmors.containsKey(player.getUniqueId().toString())) {
            player.getInventory().setArmorContents(Storage.playerArmors.get(player.getUniqueId().toString()));
            Storage.playerArmors.remove(player.getUniqueId().toString());
        }
        if(!isOldVersion(player.getInventory())) {
            if(Storage.playerExtras.containsKey(player.getUniqueId().toString())) {
                player.getInventory().setExtraContents(Storage.playerExtras.get(player.getUniqueId().toString()));
                Storage.playerExtras.remove(player.getUniqueId().toString());
            }
        }
        player.updateInventory();
        player.sendMessage(Utility.colorize(plugin.getConfig().getString("Restore Inventory.success")));

        return false;
    }

    public boolean isOldVersion(PlayerInventory inventory) {
        try {
            inventory.getExtraContents();
        }catch (NoSuchMethodError e) {
            return true;
        }
        return false;
    }
}
