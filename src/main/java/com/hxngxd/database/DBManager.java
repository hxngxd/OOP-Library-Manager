package com.hxngxd.database;

import com.hxngxd.utils.Logger;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    @Getter
    private static Connection connection = null;

    private static final String database_url = "jdbc:mysql://localhost:3306/libraryManagement";
    private static final String username = "root";
    private static final String password = "07112005";

    private DBManager() {
    }

    public static boolean connect() {
        try {
            if (!isConnected()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(database_url, username, password);
                Logger.info(DBManager.class, "Connected to the database.");
                return true;
            } else {
                Logger.info(DBManager.class, "Already connected to the database.");
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.error(DBManager.class, "Failed to connect to the database.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean disconnect() {
        try {
            if (isConnected()) {
                connection.close();
                Logger.info(DBManager.class, "Database disconnected.");
                return true;
            } else {
                Logger.info(DBManager.class, "The database is not connected.");
                return false;
            }
        } catch (SQLException e) {
            Logger.error(DBManager.class, "Failed to disconnect the database.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
