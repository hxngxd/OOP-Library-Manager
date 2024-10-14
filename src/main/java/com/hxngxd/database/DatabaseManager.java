package com.hxngxd.database;

import com.hxngxd.utils.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {

    public static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static Connection connection = null;
    private static String databaseUrl = null;
    private static String username = null;
    private static String password = null;

    private DatabaseManager() {
    }

    private static class SingletonHolder {
        private static final DatabaseManager instance = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return SingletonHolder.instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean connect() {
        try {
            if (!isConnected()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                loadDatabaseConfig();
                connection = DriverManager.getConnection(databaseUrl, username, password);
                logger.info("Successfully connected to the database");
                return true;
            } else {
                logger.info("The database is already connected");
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.info("Failed to connect to the database", e);
        }
        return false;
    }

    public boolean disconnect() {
        if (!isConnected()) {
            logger.info("The database is not connected yet");
            return false;
        }
        try {
            connection.close();
            logger.info("Database disconnected");
            return true;
        } catch (SQLException e) {
            logger.error(LogMsg.fail("disconnect from the database"), e);
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error(LogMsg.sthwr("checking database connection"), e);
            return false;
        }
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        if (!isConnected()) {
            return null;
        }
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            setParameters(pStatement, params);
            return pStatement.executeQuery();
        }
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        if (!isConnected()) {
            return 0;
        }
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            setParameters(pStatement, params);
            return pStatement.executeUpdate();
        }
    }

    private void setParameters(PreparedStatement pStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pStatement.setObject(i + 1, params[i]);
        }
    }

    private void loadDatabaseConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            databaseUrl = prop.getProperty("database.url");
            username = prop.getProperty("database.username");
            password = prop.getProperty("database.password");

            logger.info("Database configuration loaded successfully");

        } catch (IOException e) {
            logger.error("Error loading database configuration", e);
        }
    }
}
