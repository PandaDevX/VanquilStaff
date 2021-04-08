package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VanishCommand implements CommandExecutor {
    private Staff plugin;

    public VanishCommand(Staff plugin) {
        plugin.getCommand("vanish").setExecutor(this);
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // check if player
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }
        // check permission

        if(!sender.hasPermission("staff.vanish")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        Player player = (Player) sender;
        // check if the player is already vanished
        Utility.vanishPlayer(player,plugin);
        return false;
    }
}
