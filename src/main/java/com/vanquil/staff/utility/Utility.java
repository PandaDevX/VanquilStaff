package com.vanquil.staff.utility;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Utility {


    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(colorize("&c&lHey &fyou don't have permission to do that"));
    }

    public static void sendNoPermissionMessage(Player sender) {
        sender.sendMessage(colorize("&c&lHey &fyou don't have permission to do that"));
    }

    public static void sendCorrectArgument(CommandSender sender, String argument) {
        sender.sendMessage(colorize("&b/" + argument));
    }

    public static void sendCorrectArgument(Player sender, String argument) {
        sender.sendMessage(colorize("&b/" + argument));
    }

    public static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
