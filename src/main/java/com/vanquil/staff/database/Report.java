package com.vanquil.staff.database;

import com.vanquil.staff.utility.Utility;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class Report {

    private OfflinePlayer player;
    private String report;
    private OfflinePlayer reporter;
    private Location location = null;
    private String url = null;
    public Report(OfflinePlayer player, String report, OfflinePlayer reporter, Location location, String url) {
        this.player = player;
        this.report = report;
        this.reporter = reporter;
        this.location = location;
        this.url = url;
    }

    public Report(OfflinePlayer player, String report, OfflinePlayer reporter) {
        this.player = player;
        this.report = report;
        this.reporter = reporter;
    }

    public Report(OfflinePlayer player, String report, OfflinePlayer reporter, Location location) {
        this.player = player;
        this.report = report;
        this.reporter = reporter;
        this.location = location;
    }

    public Report(OfflinePlayer player, String report, OfflinePlayer reporter, String url) {
        this.player = player;
        this.report = report;
        this.reporter = reporter;
        this.url = url;
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

    public Location getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public OfflinePlayer getReportedPlayer() {
        return this.player;
    }
}
