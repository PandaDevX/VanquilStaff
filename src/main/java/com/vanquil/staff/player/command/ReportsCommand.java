package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.gui.inventory.reports.Cases;
import com.vanquil.staff.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportsCommand implements CommandExecutor {
    public ReportsCommand(Staff plugin) {
        plugin.getCommand("reports").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("staff.reports")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }
        // check if still has cool down
        Player player = (Player)sender;

        Cases cases = new Cases();
        cases.setup(1);
        cases.openInventory(player);

        player = null;
        cases = null;
        return false;
    }
}
