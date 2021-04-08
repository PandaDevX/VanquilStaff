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
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }

        Player player = (Player) sender;

        Storage.playerSelection.remove(player.getUniqueId().toString());
        Storage.staffTool.remove(player.getUniqueId().toString());

        AdminMode staffMode = new AdminMode();
        staffMode.setup();
        staffMode.openInventory(player);

        Utility.vanishStaff(player, Staff.getInstance());

        staffMode = null;
        player = null;
        return false;
    }
}
