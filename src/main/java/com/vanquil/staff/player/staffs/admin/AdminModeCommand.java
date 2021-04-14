package com.vanquil.staff.player.staffs.admin;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.gui.inventory.AdminMode;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminModeCommand implements CommandExecutor {

    public AdminModeCommand(Staff plugin) {
        plugin.getCommand("adminmode").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("staff.adminmode")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &fyou must be a player to do that"));
            return true;
        }


        Player player = (Player) sender;

        if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
            Utility.sendAdminHelp(player);
            return true;
        }

        if(Storage.staffMode.contains(player.getUniqueId().toString())) {
            Utility.vanishPlayerSilent(player, Staff.getInstance());
            sender.sendMessage(Utility.colorize("&a&lVanquil AdminMode &8>> &7Disabled"));
            player.getInventory().clear();
            player.updateInventory();

            Utility.loadInventoryStaff(player);

            Storage.staffMode.remove(player.getUniqueId().toString());
            return true;
        }

        Storage.playerSelection.remove(player.getUniqueId().toString());
        Storage.staffTool.remove(player.getUniqueId().toString());

        Utility.saveInventoryStaff(player);
        player.getInventory().clear();
        player.updateInventory();

        AdminMode staffMode = new AdminMode();
        staffMode.setup(player);

        Utility.vanishStaff(player, Staff.getInstance());

        Storage.staffMode.add(player.getUniqueId().toString());
        sender.sendMessage(Utility.colorize("&a&lVanquil AdminMode &8>> &7Enabled"));
        staffMode = null;
        player = null;
        return false;
    }
}
