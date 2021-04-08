package com.vanquil.staff.database;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PinDatabase {

    private Player player;
    public PinDatabase(Player player) {
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS pins "
                    + "(UUID VARCHAR(100), PIN VARCHAR(100), LOGGED BOOLEAN)")) {
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        this.player = player;
    }

    public void register(String pin) {
        try (PreparedStatement ps = preparedStatement("INSERT INTO pins (UUID,PIN,LOGGED) VALUES (?,?,?)")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, pin);
            ps.setBoolean(3, Staff.getInstance().getConfig().getBoolean("Auth Pin.login_after_register"));
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String pin) {
        Storage.playerIndexPin.remove(player);
        if(isRegistered()) {
            try (PreparedStatement ps = preparedStatement("SELECT * FROM pins WHERE UUID=?")) {
                ps.setString(1, player.getUniqueId().toString());
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        if (resultSet.getString("PIN").equals(pin)) {
                            log();
                            return true;
                        }
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void logout() {
        Storage.staffAttempt.remove(player.getUniqueId().toString());
        Storage.playerIndexPin.remove(player);
        if(isRegistered()) {
            try (PreparedStatement ps = preparedStatement("UPDATE pins SET LOGGED=? WHERE UUID=?")) {
                ps.setBoolean(1, false);
                ps.setString(2, player.getUniqueId().toString());
                ps.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void log() {
        try (PreparedStatement ps = preparedStatement("UPDATE pins SET LOGGED=? WHERE UUID=?")) {
            ps.setBoolean(1, true);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered() {
        try (PreparedStatement ps = preparedStatement("SELECT * FROM pins WHERE UUID=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLoggedIn() {
        if(isRegistered()) {
            try (PreparedStatement ps = preparedStatement("SELECT * FROM pins WHERE UUID=?")) {
                ps.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()) {
                    return resultSet.getBoolean("LOGGED");
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public PreparedStatement preparedStatement(String statement) throws SQLException {
        return DatabaseManager.getConnection().prepareStatement(statement);
    }
}
