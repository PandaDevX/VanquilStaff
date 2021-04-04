package com.vanquil.staff.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReportDatabase {

    public ReportDatabase() {
        try {
            PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports "
                    + "(UUID VARCHAR(100), REPORT VARCHAR(100), REPORTER VARCHAR(100), LOCATION VARCHAR(100), URL VARCHAR(100))");
            ps.executeUpdate();
            ps = null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Report> getReports(OfflinePlayer player) {
        Set<Report> reports = new HashSet<>();
        try {
            PreparedStatement ps = prepareStatement("SELECT * FROM reports WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                Location location = null;
                if(resultSet.getString("LOCATION") != null) {
                    String[] loc = resultSet.getString("LOCATION").split(",");
                    location = new Location(Bukkit.getWorld(loc[0]),
                            Double.parseDouble(loc[1]),
                            Double.parseDouble(loc[2]),
                            Double.parseDouble(loc[3]),
                            Float.parseFloat(loc[4]),
                            Float.parseFloat(loc[5]));
                    loc = null;
                }
                String report = resultSet.getString("REPORT");
                OfflinePlayer reporter = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("REPORTER")));
                String url = resultSet.getString("URL");
                Report reportObject = new Report(player, report, reporter, location, url);
                reports.add(reportObject);
                reportObject = null;
                url = null;
                reporter = null;
                report = null;
                location = null;
            }
            resultSet = null;
            ps = null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public Set<Report> getReports() {
        Set<Report> reports = new HashSet<>();
        try {
            PreparedStatement ps = prepareStatement("SELECT * FROM reports");
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                Location location = null;
                if(resultSet.getString("LOCATION") != null) {
                    String[] loc = resultSet.getString("LOCATION").split(",");
                    location = new Location(Bukkit.getWorld(loc[0]),
                            Double.parseDouble(loc[1]),
                            Double.parseDouble(loc[2]),
                            Double.parseDouble(loc[3]),
                            Float.parseFloat(loc[4]),
                            Float.parseFloat(loc[5]));
                    loc = null;
                }
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("UUID")));
                String report = resultSet.getString("REPORT");
                OfflinePlayer reporter = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("REPORTER")));
                String url = resultSet.getString("URL");
                Report reportObject = new Report(player, report, reporter, location, url);
                reports.add(reportObject);
                player = null;
                reportObject = null;
                url = null;
                reporter = null;
                report = null;
                location = null;
            }
            resultSet = null;
            ps = null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public void save(OfflinePlayer player, String report, String reporter) {
        try {
            PreparedStatement ps = prepareStatement("INSERT reports"
            + " (UUID,REPORT,REPORTER) VALUES (?,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            ps.setString(3, Bukkit.getOfflinePlayer(reporter).getUniqueId().toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(OfflinePlayer player, String report, String reporter, Location location) {
        try {
            PreparedStatement ps = prepareStatement("INSERT reports"
                    + " (UUID,REPORT,REPORTER,LOCATION) VALUES (?,?,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            ps.setString(3, Bukkit.getOfflinePlayer(reporter).getUniqueId().toString());
            ps.setString(4, location.getWorld().getName() + ","
             + location.getX() + ","
             + location.getY() + ","
             + location.getZ() + ","
             + location.getYaw() + ","
             + location.getPitch());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(OfflinePlayer player, String report, String reporter, Location location, String url) {
        try {
            PreparedStatement ps = prepareStatement("INSERT reports"
                    + " (UUID,REPORT,REPORTER,LOCATION,URL) VALUES (?,?,?,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            ps.setString(3, Bukkit.getOfflinePlayer(reporter).getUniqueId().toString());
            ps.setString(4, location.getWorld().getName() + ","
                    + location.getX() + ","
                    + location.getY() + ","
                    + location.getZ() + ","
                    + location.getYaw() + ","
                    + location.getPitch());
            ps.setString(5, url.startsWith("https://") ? url : "https://" + url);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareStatement(String statement) {
        try {
            return DatabaseManager.getConnection().prepareStatement(statement);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getNumberOfReports() {
        return getReports().size();
    }

    public int getNumberOfReports(OfflinePlayer player) {
        return getReports(player).size();
    }

}
