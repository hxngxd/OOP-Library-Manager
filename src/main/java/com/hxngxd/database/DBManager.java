package com.hxngxd.database;

import com.hxngxd.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static Connection connection = null;

    private static final String database_url = "jdbc:mysql://localhost:3306/library_management";
    private static final String username = "root";
    private static final String password = "07112005";

    public static boolean connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(database_url, username, password);
                Logger.info(DBManager.class, "Connected to database.");
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.error(DBManager.class, "Failed to connect to database.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Logger.info(DBManager.class, "Database disconnected.");
                return true;
            }
        } catch (SQLException e) {
            Logger.error(DBManager.class, "Failed to disconnect database.");
            e.printStackTrace();
        }
        return false;
    }

    public static Connection getConnection() {
        return connection;
    }
}
