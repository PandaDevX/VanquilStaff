package com.vanquil.staff.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReportDatabase {

    public ReportDatabase() {
        try {
            PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports "
                    + "(UUID VARCHAR(100), REPORT VARCHAR(100), REPORTER VARCHAR(100), URL VARCHAR(100), OPEN BOOLEAN)");
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
                Report reportObject = new Report(player, report, reporter, url, resultSet.getBoolean("OPEN"), resultSet.getString("DATE"));
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
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("UUID")));
                String report = resultSet.getString("REPORT");
                OfflinePlayer reporter = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("REPORTER")));
                String url = resultSet.getString("URL");
                Report reportObject = new Report(player, report, reporter, url, resultSet.getBoolean("OPEN"), resultSet.getString("DATE"));
                reports.add(reportObject);
                player = null;
                reportObject = null;
                url = null;
                reporter = null;
                report = null;
            }
            resultSet = null;
            ps = null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public void save(Report report) {
        try {
            PreparedStatement ps = prepareStatement("INSERT reports"
                    + " (UUID,REPORT,REPORTER,URL,OPEN,DATE) VALUES (?,?,?,?,?,?)");
            ps.setString(1, report.getReportedPlayer().getUniqueId().toString());
            ps.setString(2, report.getReport());
            ps.setString(3, report.getReporter().getUniqueId().toString());
            ps.setString(4, report.getUrl());
            ps.setBoolean(5, report.isOpen());
            ps.setString(6,  report.getDate());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(OfflinePlayer player, String report) {
        try {
            PreparedStatement ps = prepareStatement("SELECT * FROM reports WHERE UUID=? AND REPORT=?");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close(OfflinePlayer player, String report) {
        try {
            PreparedStatement ps = prepareStatement("UPDATE reports SET OPEN=? AND DATE=? WHERE UUID=? AND REPORT=?");
            ps.setBoolean(1, false);
            ps.setString(2, new SimpleDateFormat("MMMMM dd yyyy hh:mm a").format(new Date(System.currentTimeMillis())));
            ps.setString(3, player.getUniqueId().toString());
            ps.setString(4, report);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open(OfflinePlayer player, String report) {
        try {
            PreparedStatement ps = prepareStatement("UPDATE reports SET OPEN=? WHERE UUID=? AND REPORT=?");
            ps.setBoolean(1, true);
            ps.setString(2, player.getUniqueId().toString());
            ps.setString(3, report);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen(OfflinePlayer player, String report) {
        try {
            PreparedStatement ps = prepareStatement("SELECT * FROM reports WHERE UUID=? AND REPORT=?");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return resultSet.getBoolean("OPEN");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
