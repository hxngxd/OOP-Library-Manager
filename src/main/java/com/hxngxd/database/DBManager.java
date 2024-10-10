package com.hxngxd.database;

import com.hxngxd.utils.Logger;
import lombok.Getter;

import java.sql.*;

/**
 * Lớp DBManager cung cấp các phương thức để kết nối và thao tác với cơ sở dữ liệu MySQL.
 * Lớp này bao gồm các phương thức kết nối, ngắt kết nối, thực thi truy vấn SQL và cập nhật dữ liệu.
 */
public class DBManager {

    /**
     * Đối tượng Connection để lưu trữ kết nối đến cơ sở dữ liệu.
     */
    @Getter
    private static Connection connection = null;

    /**
     * URL kết nối đến cơ sở dữ liệu MySQL.
     */
    private static final String database_url = "jdbc:mysql://localhost:3306/libraryManagement";

    /**
     * Tên người dùng để kết nối cơ sở dữ liệu.
     */
    private static final String username = "root";

    /**
     * Mật khẩu để kết nối cơ sở dữ liệu.
     */
    private static final String password = "07112005";

    /**
     * Constructor riêng tư để ngăn việc tạo đối tượng DBManager.
     * Lớp này chỉ cung cấp các phương thức tĩnh để thao tác.
     */
    private DBManager() {
    }

    /**
     * Kết nối đến cơ sở dữ liệu MySQL.
     *
     * @return true nếu kết nối thành công, ngược lại trả về false.
     */
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

    /**
     * Ngắt kết nối khỏi cơ sở dữ liệu.
     *
     * @return true nếu ngắt kết nối thành công, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem có kết nối đến cơ sở dữ liệu hay không.
     *
     * @return true nếu đang kết nối, ngược lại trả về false.
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thực thi một truy vấn SQL và trả về kết quả dưới dạng ResultSet.
     *
     * @param query câu truy vấn SQL cần thực thi.
     * @param params các tham số cần truyền vào câu truy vấn.
     * @return ResultSet chứa kết quả truy vấn hoặc null nếu không kết nối.
     * @throws SQLException nếu có lỗi xảy ra khi thực thi truy vấn.
     */
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        if (!isConnected()) {
            return null;
        }
        PreparedStatement pStatement = connection.prepareStatement(query);
        setParameters(pStatement, params);
        return pStatement.executeQuery();
    }

    /**
     * Thực thi một truy vấn cập nhật (INSERT, UPDATE, DELETE) và trả về số hàng bị ảnh hưởng.
     *
     * @param query câu truy vấn SQL cần thực thi.
     * @param params các tham số cần truyền vào câu truy vấn.
     * @return số hàng bị ảnh hưởng bởi câu truy vấn.
     * @throws SQLException nếu có lỗi xảy ra khi thực thi truy vấn.
     */
    public static int executeUpdate(String query, Object... params) throws SQLException {
        if (!isConnected()) {
            return 0;
        }
        PreparedStatement pStatement = DBManager.getConnection().prepareStatement(query);
        setParameters(pStatement, params);
        return pStatement.executeUpdate();
    }

    /**
     * Đặt các tham số vào PreparedStatement cho câu truy vấn SQL.
     *
     * @param pStatement đối tượng PreparedStatement để truyền tham số.
     * @param params các tham số cần truyền vào câu truy vấn.
     * @throws SQLException nếu có lỗi xảy ra khi thiết lập tham số.
     */
    private static void setParameters(PreparedStatement pStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pStatement.setObject(i + 1, params[i]);
        }
    }
}
