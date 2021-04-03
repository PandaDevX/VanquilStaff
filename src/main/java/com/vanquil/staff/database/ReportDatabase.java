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

    private OfflinePlayer player;

    public ReportDatabase(OfflinePlayer player) {
        try {
            PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports "
                    + "(UUID VARCHAR(100), REPORT VARCHAR(100), REPORTER VARCHAR(100), LOCATION VARCHAR(100), URL VARCHAR(100))");
            ps.executeUpdate();
            ps = null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        this.player = player;
    }

    public Set<Report> getReports() {
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
                            Double.valueOf(loc[1]),
                            Double.valueOf(loc[2]),
                            Double.valueOf(loc[3]),
                            Float.valueOf(loc[4]),
                            Float.valueOf(loc[5]));
                    loc = null;
                }
                String report = resultSet.getString("REPORT");
                OfflinePlayer reporter = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("REPORTER")));
                String url = resultSet.getString("URL");
                Report reportObject = new Report(player, report, reporter, location, url);
                reports.add(reportObject);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    private PreparedStatement prepareStatement(String statement) {
        try {
            return DatabaseManager.getConnection().prepareStatement(statement);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
