package com.hxngxd.database;

import com.hxngxd.enums.LogMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private final Logger log = LogManager.getLogger(DatabaseManager.class);

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

    public Connection getConnection() {
        return connection;
    }

    public int getGeneratedId() {
        return generatedId;
    }

    public boolean connect() {
        try {
            if (!isConnected()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                loadDatabaseConfig();
                connection = DriverManager.getConnection(databaseUrl, username, password);
                log.info(LogMessages.General.SUCCESS.getMessage("connect to the database"));
                return true;
            } else {
                log.info("The database is already connected");
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.info(LogMessages.General.FAIL.getMessage("connect to the database"), e);
        }
        return false;
    }

    public boolean disconnect() {
        if (!isConnected()) {
            log.info(LogMessages.Database.NO_DB_CONNECTION);
            return false;
        }
        try {
            connection.close();
            log.info(LogMessages.General.SUCCESS.getMessage("disconnect from database"));
            return true;
        } catch (SQLException e) {
            log.error(LogMessages.General.FAIL.getMessage("disconnect from database"), e);
            return false;
        }
    }

    public static boolean isConnected() {
        try {
            return DatabaseManager.getInstance().connection != null &&
                    !DatabaseManager.getInstance().connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private void setParameters(PreparedStatement pStatement,
                               Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pStatement.setObject(i + 1, params[i]);
        }
    }

    private void loadDatabaseConfig() {
        String config = "config.properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(config)) {
            if (input == null) {
                log.error("Unable to find config.properties");
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            databaseUrl = prop.getProperty("database.url");
            username = prop.getProperty("database.username");
            password = prop.getProperty("database.password");

            log.info(LogMessages.General.SUCCESS.getMessage(
                    "load database configuration"));

        } catch (IOException e) {
            log.info(LogMessages.General.FAIL.getMessage(
                    "load database configuration"), e);
        }
    }

    public boolean update(String table, String updateField, Object updateValue,
                          String conditionField, Object conditionValue) {
        return update(table, List.of(updateField), List.of(updateValue),
                List.of(conditionField), List.of(conditionValue));
    }

    public boolean update(String table, List<String> updateFields, List<Object> updateValues,
                          List<String> conditionFields, List<Object> conditionValues) {
        StringBuilder query = new StringBuilder("update ");
        query.append(table).append(" set ");
        for (int i = 0; i < updateFields.size(); i++) {
            query.append(updateFields.get(i)).append(" = ?");
            if (i < updateFields.size() - 1) {
                query.append(", ");
            }
        }
        query.append(" where ");
        for (int i = 0; i < conditionFields.size(); i++) {
            query.append(conditionFields.get(i)).append(" = ?");
            if (i < conditionFields.size() - 1) {
                query.append(" and ");
            }
        }

        try (PreparedStatement pStatement = connection.prepareStatement(query.toString())) {
            int paramId = 1;
            for (Object value : updateValues) {
                pStatement.setObject(paramId, value);
                paramId++;
            }
            for (Object condition : conditionValues) {
                pStatement.setObject(paramId, condition);
                paramId++;
            }
            int updates = pStatement.executeUpdate();
            if (updates > 0) {
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            log.error(LogMessages.General.SOMETHING_WENT_WRONG.getMessage(
                    "updating database"), e);
        }
        return false;
    }

    public boolean insert(String table, List<String> conditionFields, Object... conditionValues) {
        StringBuilder query = new StringBuilder("insert into ");
        query.append(table).append("(");
        for (int i = 0; i < conditionFields.size(); i++) {
            query.append(conditionFields.get(i));
            if (i != conditionFields.size() - 1) {
                query.append(", ");
            } else {
                query.append(")");
            }
        }
        query.append(" value (");
        for (int i = 0; i < conditionFields.size(); i++) {
            query.append("?");
            if (i != conditionFields.size() - 1) {
                query.append(", ");
            } else {
                query.append(")");
            }
        }
        try (PreparedStatement pStatement = connection.prepareStatement(query.toString(),
                Statement.RETURN_GENERATED_KEYS)) {
            setParameters(pStatement, conditionValues);
            int updates = pStatement.executeUpdate();
            if (updates > 0) {
                try (ResultSet resultSet = pStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt(1);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            log.error(LogMessages.General.SOMETHING_WENT_WRONG.getMessage(
                    "inserting into database"), e);
        }
        return true;
    }

    public boolean delete(String table, String conditionField, Object conditionValue) {
        return delete(table, List.of(conditionField), conditionValue);
    }

    public boolean delete(String table, List<String> conditionFields,
                          Object... conditionValues) {
        StringBuilder query = new StringBuilder("delete from ");
        query.append(table).append(" where ");
        for (int i = 0; i < conditionFields.size(); i++) {
            query.append(conditionFields.get(i)).append(" = ?");
            if (i < conditionFields.size() - 1) {
                query.append(" and ");
            }
        }
        try (PreparedStatement pStatement = connection.prepareStatement(query.toString())) {
            setParameters(pStatement, conditionValues);
            int updates = pStatement.executeUpdate();
            if (updates > 0) {
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            log.error(LogMessages.General.SOMETHING_WENT_WRONG.getMessage(
                    "deleting from database"), e);
        }
        return false;
    }

    public <T> T select(String action, String query,
                        ResultSetMapper<T> mapper, Object... params) {
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            setParameters(pStatement, params);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                return mapper.map(resultSet);
            }
        } catch (SQLException e) {
            log.error(LogMessages.General.SOMETHING_WENT_WRONG.getMessage(
                    "executing query"), e);
        }
        return null;
    }
}