package com.vanquil.staff.player.command;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.Report;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportCommand implements CommandExecutor {

    public ReportCommand(Staff plugin) {
        plugin.getCommand("report").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("player.report")) {
            Utility.sendNoPermissionMessage(sender);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utility.colorize("&c&lHey &fyou must be a player to do that"));
            return true;
        }

        if(args.length < 2) {
            Utility.sendCorrectArgument(sender, "report (player) (reason)");
            return true;
        }
        // check if still has cool down
        Player player = (Player)sender;
        if(Storage.playerReportCoolDown.containsKey(player.getUniqueId().toString())) {

            if(Storage.playerReportCoolDown.get(player.getUniqueId().toString()) > System.currentTimeMillis()) {

                player.sendMessage(Utility.colorize("&c&lHey &fyou can only send report every &62 minutes"));
                return true;
            }
        }
        OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(args[0]);

        if(!reportedPlayer.hasPlayedBefore()) {
            sender.sendMessage(Utility.colorize("&c&lHey &fthat player did not play here before"));
            return true;
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        Report report = new Report(reportedPlayer, builder.toString().trim(), player, true, new SimpleDateFormat("MMMMM dd yyyy hh:mm a").format(new Date(System.currentTimeMillis())));


        Storage.playerReport.put(player.getUniqueId().toString(), report);

        com.vanquil.staff.gui.inventory.Report gui = new com.vanquil.staff.gui.inventory.Report();
        gui.setup(report);
        gui.openInventory(player);

        gui = null;
        player = null;
        report = null;
        return false;
    }
}
