package com.hxngxd.database;

import com.hxngxd.enums.LogMsg;
import com.hxngxd.exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public final class DatabaseManager {

    private static final Logger log = LogManager.getLogger(DatabaseManager.class);

    private Connection connection = null;
    private String databaseUrl = null;
    private String username = null;
    private String password = null;
    private int generatedId;

    private DatabaseManager() {
    }

    private static class SingletonHolder {
        private static final DatabaseManager instance = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return SingletonHolder.instance;
    }

    public int getGeneratedId() {
        return generatedId;
    }

    public boolean connect() {
        if (isConnected()) {
            log.info("The database is already connected");
            return false;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            loadDatabaseConfig();
            connection = DriverManager.getConnection(databaseUrl, username, password);
            log.info(LogMsg.GENERAL_SUCCESS.msg("connect to the database"));
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            log.info(LogMsg.GENERAL_FAIL.msg("connect to the database"), e);
            return false;
        }
    }

    public boolean disconnect() {
        if (!isConnected()) {
            log.info(LogMsg.DATABASE_NO_CONNECTION);
            return false;
        }
        try {
            connection.close();
            log.info(LogMsg.GENERAL_SUCCESS.msg("disconnect from database"));
            return true;
        } catch (SQLException e) {
            log.error(LogMsg.GENERAL_FAIL.msg("disconnect from database"), e);
            return false;
        }
    }

    public static boolean isConnected() {
        try {
            DatabaseManager instance = DatabaseManager.getInstance();
            return instance.connection != null && !instance.connection.isClosed();
        } catch (SQLException e) {
            log.error("Failed to check database connection", e);
            return false;
        }
    }

    private void setParameters(PreparedStatement pStatement, Object... params)
            throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pStatement.setObject(i + 1, params[i]);
        }
    }

    private void loadDatabaseConfig() {
        String config = "config.properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(config)) {
            if (input == null) {
                log.error(LogMsg.FILE_NOT_FOUND.msg("config.properties"));
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            databaseUrl = prop.getProperty("database.url");
            username = prop.getProperty("database.username");
            password = prop.getProperty("database.password");

            log.info(LogMsg.GENERAL_SUCCESS.msg("load database configuration"));
        } catch (IOException e) {
            log.info(LogMsg.GENERAL_FAIL.msg("load database configuration"), e);
        }
    }

    public void update(String table, String updateField, Object updateValue,
                       String conditionField, Object conditionValue)
            throws DatabaseException {
        update(table, List.of(updateField), List.of(updateValue), List.of(conditionField), List.of(conditionValue));
    }

    public void update(String table, List<String> updateFields, List<Object> updateValues,
                       List<String> conditionFields, List<Object> conditionValues)
            throws DatabaseException {
        String updatePart = String.join(", ", updateFields.stream().map(f -> f + " = ?").toList());
        String conditionPart = String.join(" and ", conditionFields.stream().map(f -> f + " = ?").toList());
        String query = String.format("update %s set %s where %s", table, updatePart, conditionPart);

        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            int paramId = 1;
            for (Object value : updateValues) {
                pStatement.setObject(paramId++, value);
            }
            for (Object condition : conditionValues) {
                pStatement.setObject(paramId++, condition);
            }
            if (pStatement.executeUpdate() < 1) {
                throw new SQLException(LogMsg.GENERAL_SOMETHING_WENT_WRONG.msg("executing update query"));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void insert(String table, boolean getId, List<String> fields, Object... values) throws DatabaseException {
        String fieldsPart = String.join(", ", fields);
        String valuesPart = String.join(", ", fields.stream().map(f -> "?").toList());
        String query = String.format("insert into %s (%s) values (%s)", table, fieldsPart, valuesPart);

        try (PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(pStatement, values);
            int updates = pStatement.executeUpdate();

            if (updates < 1) {
                throw new SQLException(LogMsg.GENERAL_SOMETHING_WENT_WRONG.msg("executing insert query"));
            } else if (getId) {
                try (ResultSet resultSet = pStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void delete(String table, String conditionField, Object conditionValue)
            throws DatabaseException {
        delete(table, List.of(conditionField), conditionValue);
    }

    public void delete(String table, List<String> conditionFields, Object... conditionValues) throws DatabaseException {
        String conditionPart = String.join(" and ", conditionFields.stream().map(f -> f + " = ?").toList());
        String query = String.format("delete from %s where %s", table, conditionPart);

        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            setParameters(pStatement, conditionValues);
            int updates = pStatement.executeUpdate();

            if (updates < 1) {
                throw new SQLException(LogMsg.GENERAL_SOMETHING_WENT_WRONG.msg("executing delete query"));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public <T> T select(String action, String query, ResultSetMapper<T> mapper, Object... params) throws DatabaseException {
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            setParameters(pStatement, params);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                return mapper.map(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}