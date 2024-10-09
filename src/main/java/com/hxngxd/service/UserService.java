package com.hxngxd.service;

import com.hxngxd.database.DBManager;
import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;
import com.hxngxd.utils.Logger;
import com.hxngxd.utils.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Lớp UserService cung cấp các chức năng quản lý người dùng bao gồm đăng ký, đăng nhập, chỉnh sửa thông tin và quản lý xác thực.
 */
public class UserService {

    // Thuộc tính
    private static User currentUser;
    private static String passwordHash;
    private static boolean twoFactorEnabled;

    /**
     * Kiểm tra xem người dùng hiện tại đã đăng nhập hay chưa.
     *
     * @return true nếu người dùng hiện tại đã đăng nhập, false nếu không.
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Đăng ký tài khoản mới.
     *
     * @param newUser Thông tin người dùng mới.
     * @return true nếu đăng ký thành công, false nếu thất bại.
     */
    public static boolean register(User newUser) {
        return true;
    }

    /**
     * Đăng nhập vào hệ thống.
     *
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return true nếu đăng nhập thành công, false nếu thất bại.
     */
    public static boolean loginByUsername(String username, String password) {
        if (isLoggedIn()) {
            return false;
        }
        Logger.info(UserService.class, "Try logging in by username: " + username + "...");
        return login(getUserByUsername(username), password);
    }

    /**
     * Đăng nhập vào hệ thống.
     *
     * @param email    Địa chỉ email.
     * @param password Mật khẩu.
     * @return true nếu đăng nhập thành công, false nếu thất bại.
     */
    public static boolean loginByEmail(String email, String password) {
        if (isLoggedIn()) {
            return false;
        }
        Logger.info(UserService.class, "Try logging in by email: " + email + "...");
        return login(getUserByEmail(email), password);
    }

    /**
     * Đăng nhập cho người dùng với mật khẩu được cung cấp.
     *
     * @param user     Đối tượng người dùng cần đăng nhập.
     * @param password Mật khẩu của người dùng.
     * @return true nếu đăng nhập thành công, false nếu tài khoản không tồn tại hoặc mật khẩu sai.
     */
    private static boolean login(User user, String password) {
        if (!DBManager.isConnected()) {
            Logger.info(UserService.class, "Can not log in because the database is not connected.");
            return false;
        }

        if (user == null) {
            Logger.info(UserService.class, "User does not exist.");
            return false;
        } else {
            if (PasswordEncoder.compare(password, passwordHash)) {
                if (!twoFactorEnabled) {
                    Logger.info(UserService.class, "Logged in.");
                    currentUser = user;
                    return true;
                } else {
                    Logger.info(UserService.class, "Two facter authenciation required.");
                    return false;
                }
            } else {
                Logger.info(UserService.class, "Wrong password.");
                return false;
            }
        }
    }

    /**
     * Đăng xuất khỏi hệ thống.
     *
     * @return true nếu đăng xuất thành công.
     */
    public static boolean logout() {
        return true;
    }

    /**
     * Bật/tắt xác thực 2 bước.
     *
     * @return true nếu thao tác thành công.
     */
    public static boolean toggleTwoFactorAuthentication() {
        return true;
    }

    /**
     * Thay đổi tên của người dùng.
     *
     * @param personId     ID của người dùng.
     * @param newFirstName Tên mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeFirstName(int personId, String newFirstName) {
        return true;
    }

    /**
     * Thay đổi họ của người dùng.
     *
     * @param personId    ID của người dùng.
     * @param newLastName Họ mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeLastName(int personId, String newLastName) {
        return true;
    }

    /**
     * Thay đổi ngày sinh của người dùng.
     *
     * @param personId ID của người dùng.
     * @param newDate  Ngày sinh mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeDateOfBirth(int personId, LocalDate newDate) {
        return true;
    }

    /**
     * Thay đổi ảnh đại diện của người dùng.
     *
     * @param personId ID của người dùng.
     * @param photoURL URL ảnh đại diện mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changePhoto(int personId, String photoURL) {
        return true;
    }

    /**
     * Thay đổi email của người dùng.
     *
     * @param userId   ID của người dùng.
     * @param newEmail Email mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeEmail(int userId, String newEmail) {
        return true;
    }

    /**
     * Thay đổi địa chỉ của người dùng.
     *
     * @param userId     ID của người dùng.
     * @param newAddress Địa chỉ mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeAddress(int userId, String newAddress) {
        return true;
    }

    /**
     * Thay đổi chức vụ của người dùng (trừ Admin).
     *
     * @param userId ID của người dùng.
     * @param role   Chức vụ mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeRole(int userId, Role role) {
        return true;
    }

    /**
     * Thay đổi trạng thái tài khoản của người dùng (trừ Admin).
     *
     * @param userId ID của người dùng.
     * @param status Trạng thái tài khoản mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeAccountStatus(int userId, AccountStatus status) {
        return true;
    }

    /**
     * Xử lý khi người dùng vi phạm.
     *
     * @param userId ID của người dùng vi phạm.
     * @return true nếu xử lý thành công.
     */
    public static boolean violate(int userId) {
        return true;
    }

    /**
     * Thay đổi mật khẩu của mình.
     *
     * @param oldPassword Mật khẩu cũ.
     * @param newPassword Mật khẩu mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changePassword(String oldPassword, String newPassword) {
        return true;
    }

    /**
     * Thay đổi mật khẩu của tài khoản người dùng khác (trừ Admin).
     *
     * @param userId      ID của người dùng.
     * @param newPassword Mật khẩu mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changePassword(int userId, String newPassword) {
        return true;
    }

    /**
     * Yêu cầu phục hồi mật khẩu qua email.
     *
     * @param email Địa chỉ email cần khôi phục.
     * @return true nếu yêu cầu thành công.
     */
    public static boolean resetPasswordRequest(String email) {
        return true;
    }

    /**
     * Yêu cầu mã xác minh qua địa chỉ email.
     *
     * @param email Địa chỉ email.
     * @return true nếu yêu cầu thành công.
     */
    public static boolean emailOTPRequest(String email) {
        return true;
    }

    /**
     * Xác minh địa chỉ email bằng mã OTP.
     *
     * @param email Địa chỉ email cần xác minh.
     * @param OTP   Mã xác minh.
     * @return true nếu xác minh thành công.
     */
    public static boolean verifyEmail(String email, String OTP) {
        return true;
    }

    /**
     * Xoá tài khoản của mình.
     *
     * @param password Mật khẩu.
     * @param OTP      Mã xác minh.
     * @return true nếu xoá thành công.
     */
    public static boolean deleteAccount(String password, String OTP) {
        return true;
    }

    /**
     * Xoá tài khoản (Admin).
     *
     * @param userId ID của tài khoản cần xoá.
     * @return true nếu xoá thành công.
     */
    public static boolean deleteAccount(int userId) {
        return true;
    }

    /**
     * Kiểm tra quyền hạn của người dùng hiện tại.
     *
     * @param permission Quyền hạn cần kiểm tra.
     * @return true nếu người dùng có quyền, false nếu không.
     */
    public static boolean hasPermission(Permission permission) {
        return true;
    }

    /**
     * Lấy thông tin người dùng bằng ID.
     *
     * @param id ID của người dùng.
     * @return Thông tin người dùng.
     */
    public static User getUserById(int id) {
        return getUserByUniqueField("id", String.valueOf(id));
    }

    /**
     * Lấy thông tin người dùng bằng tên đăng nhập.
     *
     * @param username Tên đăng nhập của người dùng.
     * @return Thông tin người dùng.
     */
    public static User getUserByUsername(String username) {
        return getUserByUniqueField("username", username);
    }

    /**
     * Lấy thông tin người dùng bằng địa chỉ email.
     *
     * @param email Địa chỉ email của người dùng.
     * @return Thông tin người dùng.
     */
    public static User getUserByEmail(String email) {
        return getUserByUniqueField("email", email);
    }

    /**
     * Lấy thông tin người dùng bằng một trường duy nhất.
     *
     * @param field Trường duy nhất cần truy vấn.
     * @param info  Giá trị của trường cần tìm.
     * @return Thông tin người dùng nếu tìm thấy, null nếu không tìm thấy.
     */
    private static User getUserByUniqueField(String field, String info) {
        if (!DBManager.isConnected()) {
            return null;
        }

        String query = "select * from user where " + field + " = ?";
        try (PreparedStatement pStatement = DBManager.getConnection().prepareStatement(query)) {
            pStatement.setString(1, info);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                if (resultSet.next()) {
                    passwordHash = resultSet.getString("passwordHash");
                    twoFactorEnabled = resultSet.getBoolean("twoFactorEnabled");
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getDate("dateOfBirth").toLocalDate(),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("address"),
                            Role.valueOf(resultSet.getString("role")),
                            AccountStatus.valueOf(resultSet.getString("accountStatus")),
                            resultSet.getInt("violationCount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}