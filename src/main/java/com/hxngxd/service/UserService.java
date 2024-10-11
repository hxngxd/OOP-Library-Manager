package com.hxngxd.service;

import com.hxngxd.database.DBManager;
import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;
import com.hxngxd.utils.EmailValidator;
import com.hxngxd.utils.PasswordEncoder;
import com.hxngxd.utils.LogMsg;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Lớp UserService cung cấp các chức năng quản lý người dùng bao gồm đăng ký, đăng nhập, chỉnh sửa thông tin và quản lý xác thực.
 */
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private static User currentUser;
    private static String passwordHash;

    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Kiểm tra xem người dùng hiện tại đã đăng nhập hay chưa.
     *
     * @return true nếu người dùng hiện tại đã đăng nhập, false nếu không.
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Phương thức đăng ký người dùng mới.
     * <p>
     * Phương thức này sẽ thực hiện đăng ký tài khoản mới cho người dùng nếu như
     * các điều kiện được đáp ứng và không có lỗi xảy ra. Trước khi đăng ký,
     * phương thức sẽ kiểm tra các điều kiện như: người dùng đã đăng nhập hay chưa,
     * kết nối cơ sở dữ liệu có sẵn hay không, thông tin người dùng có hợp lệ không,
     * tài khoản đã tồn tại chưa, mật khẩu có khớp hay không.
     *
     * @param firstName         Tên người dùng.
     * @param lastName          Họ của người dùng.
     * @param dateOfBirth       Ngày sinh của người dùng.
     * @param username          Tên đăng nhập của người dùng.
     * @param email             Địa chỉ email của người dùng.
     * @param address           Địa chỉ nhà của người dùng.
     * @param password          Mật khẩu mà người dùng chọn.
     * @param confirmedPassword Mật khẩu xác nhận lại.
     * @return {@code true} nếu đăng ký thành công, ngược lại {@code false}.
     * <p>
     * Các điều kiện trả về {@code false}:
     * - Người dùng hiện tại đã đăng nhập.
     * - Kết nối cơ sở dữ liệu không khả dụng.
     * - Một số thông tin như họ, tên, tên đăng nhập, email hoặc địa chỉ quá dài.
     * - Địa chỉ email không hợp lệ.
     * - Người dùng đã tồn tại trong hệ thống với tên đăng nhập hoặc email đã cho.
     * - Mật khẩu và mật khẩu xác nhận không khớp.
     * - Có lỗi trong quá trình tạo tài khoản trong cơ sở dữ liệu.
     */
    public static boolean register(String firstName, String lastName, LocalDate dateOfBirth, String username, String email, String address, String password, String confirmedPassword) {
        if (isLoggedIn()) {
            logger.info(LogMsg.userNotLogOut);
            return false;
        }

        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("register"));
            return false;
        }

        if (firstName.isEmpty() || lastName.isEmpty() ||
                username.isEmpty() || email.isEmpty() ||
                address.isEmpty()) {
            logger.info("Some information is missing");
            return false;
        }

        if (firstName.length() > 127 || lastName.length() > 127 ||
                username.length() > 127 || email.length() > 127 ||
                address.length() > 255) {
            logger.info("Some information is too long");
            return false;
        }

        if (!EmailValidator.validate(email)) {
            logger.info("The provided email is not valid");
            return false;
        }

        String query = "select count(*) as countAccount from user where username = ? or email = ?";
        try (ResultSet resultSet = DBManager.executeQuery(query, username, email)) {
            if (resultSet != null && resultSet.next()) {
                if (resultSet.getInt("countAccount") > 0) {
                    logger.info(LogMsg.userExist);
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error(LogMsg.smthwr("creating your account"), e);
            return false;
        }

        if (!confirmedPassword.equals(password)) {
            logger.info("Reconfirm your password");
            return false;
        }

        passwordHash = PasswordEncoder.encode(confirmedPassword);

        query = "insert into user(firstName, lastName, dateOfBirth, username, email, address, role, passwordHash, accountStatus) value (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int updates = DBManager.executeUpdate(query, firstName, lastName,
                    Date.valueOf(dateOfBirth), username, email,
                    address, Role.USER.name(), passwordHash, AccountStatus.ACTIVE.name());

            if (updates < 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.error(LogMsg.smthwr("creating your account"), e);
            return false;
        }

        currentUser = new User(getNumberOfUsers(), firstName, lastName,
                dateOfBirth, username, email,
                address, Role.USER, AccountStatus.ACTIVE, 0);

        logger.info(LogMsg.success("created account"));
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
            logger.info(LogMsg.userNotLogOut);
            return false;
        }
        logger.info("Try logging in by username: {}...", username);
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
            logger.info(LogMsg.userNotLogOut);
            return false;
        }
        logger.info("Try logging in by email: {}...", email);
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
            logger.info(LogMsg.noDBConnection("log in"));
            return false;
        }

        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (!PasswordEncoder.compare(password, passwordHash)) {
            logger.info(LogMsg.wrongPW);
            return false;
        }

        if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
            logger.info("User is suspended");
            return false;
        }

        if (user.getAccountStatus() == AccountStatus.BANNED) {
            logger.info("User is banned");
            return false;
        }

        String query = "update user set accountStatus = ? where id = ?";
        try {
            int updates = DBManager.executeUpdate(query, AccountStatus.ACTIVE.name(), user.getId());
            if (updates >= 1) {
                currentUser = user;
                logger.info(LogMsg.success("logged in"));
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            currentUser = null;
            passwordHash = null;
            logger.error(LogMsg.fail("log in"), e);
            return false;
        }
    }

    /**
     * Đăng xuất khỏi hệ thống.
     *
     * @return true nếu đăng xuất thành công.
     */
    public static boolean logout() {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("log out"));
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        String query = "update user set lastActive = ?, accountStatus = ? where id = ?";
        try {
            int updates = DBManager.executeUpdate(query, Timestamp.valueOf(LocalDateTime.now()), AccountStatus.INACTIVE.name(), currentUser.getId());
            if (updates >= 1) {
                currentUser = null;
                passwordHash = "";
                logger.info(LogMsg.success("logged out"));
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.error(LogMsg.fail("log out"), e);
            return false;
        }
    }

    /**
     * Thay đổi tên của người dùng.
     *
     * @param userId       ID của người dùng.
     * @param newFirstName Tên mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean updateProfile(int userId, String newFirstName, String newLastName, LocalDate newDateOfBirth, String newAddress) {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("update profile"));
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (userId != currentUser.getId()) {
            if (currentUser.getRole().hasPermission(Permission.EDIT_OTHERS_PROFILE)) {
                logger.info(LogMsg.userNotAllowTo("change others' profile"));
                return false;
            }

            User other = getUserById(userId);
            if (other == null) {
                logger.info(LogMsg.userNotFound);
                return false;
            } else {
                if (other.getRole() == Role.ADMIN) {
                    logger.info(LogMsg.userCant("change other Admins' profile"));
                    return false;
                }
            }
        }

        String query = "update user set firstName = ?, lastName = ?, dateOfBirth = ?, address = ? where id = ?";
        try {
            int updates = DBManager.executeUpdate(query,
                    newFirstName, newLastName,
                    Date.valueOf(newDateOfBirth), newAddress, userId);
            if (updates >= 1) {
                logger.info(LogMsg.success("updated profile"));
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.error(LogMsg.fail("update profile"), e);
            return false;
        }
    }

    /**
     * Thay đổi email của người dùng.
     *
     * @param userId   ID của người dùng.
     * @param newEmail Email mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeEmail(int userId, String newEmail) {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("update profile"));
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }
        return true;
    }

    /**
     * Thay đổi chức vụ của người dùng (trừ Admin).
     *
     * @param userId ID của người dùng.
     * @param role   Chức vụ mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeOthersRole(int userId, Role role) {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("change role"));
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHERS_ROLE)) {
            logger.info(LogMsg.userNotAllowTo("change others' role"));
            return false;
        }

        if (userId == currentUser.getId()) {
            logger.info(LogMsg.userCant("change their own role"));
            return false;
        }

        User other = getUserById(userId);
        if (other == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (other.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("change others' Admin role"));
            return false;
        }

        String query = "update user set role = ? where id = ?";
        try {
            int updates = DBManager.executeUpdate(query, role.name(), userId);
            if (updates >= 1) {
                logger.info(LogMsg.success("changed role"));
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.error(LogMsg.fail("change role"), e);
            return false;
        }
    }

    /**
     * Thay đổi trạng thái tài khoản của người dùng (trừ Admin).
     *
     * @param userId ID của người dùng.
     * @param status Trạng thái tài khoản mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeOthersAccountStatus(int userId, AccountStatus status) {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("change account status"));
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHERS_ACCOUNT_STATUS)) {
            logger.info(LogMsg.userNotAllowTo("change others' account status"));
            return false;
        }

        if (status == AccountStatus.ACTIVE || status == AccountStatus.INACTIVE) {
            logger.info(LogMsg.userDontHaveTo);
            return false;
        }

        if (userId == currentUser.getId()) {
            logger.info(LogMsg.userCant("set their own account status to SUSPENDED or BANNED"));
            return false;
        }

        User other = getUserById(userId);
        if (other == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (other.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("change others' Admin account status"));
            return false;
        }

        String query = "update user set accountStatus = ? where id = ?";
        try {
            int updates = DBManager.executeUpdate(query, status.name(), userId);
            if (updates >= 1) {
                logger.info(LogMsg.success("changed account status"));
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.error(LogMsg.fail("change account status"), e);
            return false;
        }
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
            logger.info(LogMsg.noDBConnection("find user"));
            return null;
        }

        String query = "select * from user where " + field + " = ?";
        try (ResultSet resultSet = DBManager.executeQuery(query, info)) {
            if (resultSet != null && resultSet.next()) {
                passwordHash = resultSet.getString("passwordHash");
                logger.info("Found user by {}: {}", field, info);
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
        } catch (SQLException e) {
            logger.error(LogMsg.smthwr("finding user"));
        }
        return null;
    }

    private static int getNumberOfUsers() {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("get number of users"));
            return -1;
        }

        String query = "select count(*) as total from user";
        try (ResultSet resultSet = DBManager.executeQuery(query)) {
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            logger.error(LogMsg.smthwr("getting number of users"));
        }

        return -1;
    }

}