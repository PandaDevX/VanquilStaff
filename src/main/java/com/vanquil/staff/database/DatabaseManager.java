package com.vanquil.staff.database;

import org.bukkit.configuration.file.FileConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {

    private static Connection connection = null;

    public static boolean isConnected() {
        return connection != null;
    }

    public static void connect(FileConfiguration config) throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            String host = config.getString("MySQL.host");
            String user = config.getString("MySQL.username");
            String pass = config.getString("MySQL.password");
            String database = config.getString("MySQL.database");
            String port = config.getString("MySQL.port");
            connection = DriverManager.getConnection("jdbc:mysql://"
                    + host + ":" + port + "/" + database + "?useSSL=false",
                    user, pass);

        }
    }

    public static void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
