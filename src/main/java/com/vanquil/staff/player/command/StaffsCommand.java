package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.player.staffs.StaffGui;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffsCommand implements CommandExecutor {

    public StaffsCommand(Staff plugin) {
        plugin.getCommand("staffs").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check if player
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &fyou must be a player to do that"));
            return true;
        }

        // check permission

        if(!sender.hasPermission("staff.stafflist")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        StaffGui staffGui = new StaffGui();
        staffGui.setup((Player) sender);
        staffGui.openInventory(((Player) sender));

        staffGui = null;

        return false;
    }
}
