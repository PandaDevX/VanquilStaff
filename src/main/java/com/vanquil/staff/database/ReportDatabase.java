package com.vanquil.staff.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportDatabase {

    public ReportDatabase() {
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports "
                    + "(UUID VARCHAR(100), REPORT VARCHAR(100), REPORTER VARCHAR(100), URL VARCHAR(100), OPEN BOOLEAN, DATE VARCHAR(100))")) {
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Report> getReports(OfflinePlayer player) {
        Set<Report> reports = new HashSet<>();
        try (PreparedStatement ps = prepareStatement("SELECT * FROM reports WHERE UUID=?")) {
            ps.setString(1, player.getUniqueId().toString());
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String report = resultSet.getString("REPORT");
                    OfflinePlayer reporter = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("REPORTER")));
                    String url = resultSet.getString("URL");
                    Report reportObject = new Report(player, report, reporter, url, resultSet.getBoolean("OPEN"), resultSet.getString("DATE"));
                    reports.add(reportObject);
                    reportObject = null;
                    url = null;
                    reporter = null;
                    report = null;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public Set<String> getReportedPlayers() {
        Set<String> reportedPlayers = new HashSet<>();
        try (PreparedStatement ps = prepareStatement("SELECT * FROM reports")) {
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    reportedPlayers.add(resultSet.getString("UUID"));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reportedPlayers;
    }

    public Set<Report> getReports() {
        Set<Report> reports = new HashSet<>();
        try (PreparedStatement ps = prepareStatement("SELECT * FROM reports")) {
            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
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
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public void save(Report report) {
        try (PreparedStatement ps = prepareStatement("INSERT reports"
                    + " (UUID,REPORT,REPORTER,URL,OPEN,DATE) VALUES (?,?,?,?,?,?)")) {
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
        try (PreparedStatement ps = prepareStatement("SELECT * FROM reports WHERE UUID=? AND REPORT=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            try(ResultSet resultSet = ps.executeQuery()) {
                return resultSet.next();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close(OfflinePlayer player, String report, String date) {
        try (PreparedStatement ps = prepareStatement("UPDATE reports SET OPEN=? WHERE UUID=? AND REPORT=? AND DATE=?")) {
            ps.setBoolean(1, false);
            ps.setString(2, player.getUniqueId().toString());
            ps.setString(3, report);
            ps.setString(4, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open(OfflinePlayer player, String report) {
        try (PreparedStatement ps = prepareStatement("UPDATE reports SET OPEN=? WHERE UUID=? AND REPORT=?")) {
            ps.setBoolean(1, true);
            ps.setString(2, player.getUniqueId().toString());
            ps.setString(3, report);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen(OfflinePlayer player, String report) {
        try (PreparedStatement ps = prepareStatement("SELECT * FROM reports WHERE UUID=? AND REPORT=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, report);
            try(ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("OPEN");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PreparedStatement prepareStatement(String statement) throws SQLException {
        return DatabaseManager.getConnection().prepareStatement(statement);
    }

    public int getNumberOfOpenReports(OfflinePlayer player) {
        List<Report> openReports = new ArrayList<>();

        for(Report report : getReports(player)) {

            if(report.isOpen()) {
                openReports.add(report);
                continue;
            }
        }

        if(openReports.isEmpty()) {
            return 0;
        }
        return openReports.size();
    }

    public int getNumberOfReports() {
        return getReports().size();
    }

    public int getNumberOfReports(OfflinePlayer player) {
        return getReports(player).size();
    }

}
