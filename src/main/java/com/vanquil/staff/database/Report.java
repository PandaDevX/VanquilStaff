package com.vanquil.staff.database;

import com.vanquil.staff.utility.Utility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Report {

    private OfflinePlayer player;
    private String report;
    private OfflinePlayer reporter;
    private String url = null;
    private boolean open;
    private String date;
    public Report(OfflinePlayer player, String report, OfflinePlayer reporter, String url, boolean open, String date) {
        this.player = player;
        this.report = report;
        this.reporter = reporter;
        this.url = url;
        this.open = open;
        this.date = date;
    }

    public Report(OfflinePlayer player, String report, OfflinePlayer reporter, boolean open, String date) {
        this.player = player;
        this.report = report;
        this.reporter = reporter;
        this.open = open;
        this.date = date;
    }

    public ItemStack getPlayerHead() {
        return Utility.getSkull(player, "&b" + player.getName());
    }

    public ItemStack getReporterHead() {
        return Utility.getSkull(reporter, "&b" + reporter.getName());
    }

    public String getReport() {
        return report;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public OfflinePlayer getReportedPlayer() {
        return this.player;
    }

    public OfflinePlayer getReporter() {
        return reporter;
    }

    public boolean isOpen() {
        return open;
    }

    public void save() {
        ReportDatabase reportDatabase = new ReportDatabase();
        reportDatabase.save(this);
        reportDatabase = null;

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(Utility.getStaffNames().contains(player.getName())) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utility.colorize("&a&lVanquil &8>> &7Check out the new report")));
            }
        }
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void setReason(String report) {
        this.report = report;
    }
}
