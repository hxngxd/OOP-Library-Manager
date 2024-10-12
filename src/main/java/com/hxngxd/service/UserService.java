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
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection("register"));
            return false;
        }

        if (isLoggedIn()) {
            logger.info(LogMsg.userNotLogOut);
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

        if (getUserByUsername(username) != null || getUserByEmail(email) != null) {
            logger.info(LogMsg.userExist);
            return false;
        }

        if (!confirmedPassword.equals(password)) {
            logger.info("Reconfirm your password");
            return false;
        }

        passwordHash = PasswordEncoder.encode(confirmedPassword);

        String query = "insert into user(firstName, lastName, dateOfBirth, " +
                " username, email, address, role, passwordHash, accountStatus) " +
                "value (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if (!executeUserQuery("register", query, firstName, lastName,
                Date.valueOf(dateOfBirth), username, email,
                address, Role.USER.name(), passwordHash, AccountStatus.ACTIVE.name())) {
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
        if (executeUserQuery("log in", query, AccountStatus.ACTIVE.name(), user.getId())) {
            currentUser = user;
            return true;
        } else {
            currentUser = null;
            passwordHash = null;
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
        if (executeUserQuery("log out", query, Timestamp.valueOf(LocalDateTime.now()), AccountStatus.INACTIVE.name(), currentUser.getId())) {
            currentUser = null;
            passwordHash = "";
            return true;
        } else {
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
        if (!isLogInAndConnectedToTheDB("update profile")) {
            return false;
        }

        if (userId != currentUser.getId()) {
            if (!currentUser.getRole().hasPermission(Permission.EDIT_OTHERS_PROFILE)) {
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
        return executeUserQuery("update profile", query, newFirstName, newLastName,
                Date.valueOf(newDateOfBirth), newAddress, userId);
    }

    /**
     * Thay đổi email của người dùng.
     *
     * @param userId   ID của người dùng.
     * @param newEmail Email mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeEmail(int userId, String newEmail) {
        if (!isLogInAndConnectedToTheDB("change email")) {
            return false;
        }

        if (userId != currentUser.getId()) {
            if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
                logger.info(LogMsg.userNotAllowTo("change others' email"));
                return false;
            }

            User other = getUserById(userId);
            if (other == null) {
                logger.info(LogMsg.userNotFound);
                return false;
            } else {
                if (other.getRole() == Role.ADMIN) {
                    logger.info(LogMsg.userCant("change other Admins' email"));
                    return false;
                }
            }
        }

        if (!EmailValidator.validate(newEmail)) {
            logger.info("Email is not valid");
            return false;
        }

        if (getUserByEmail(newEmail) != null) {
            logger.info("Email is already used by other users");
            return false;
        }

        String query = "update user set email = ? where id = ?";
        return executeUserQuery("change email", query, newEmail, userId);
    }

    /**
     * Thay đổi chức vụ của người dùng (trừ Admin).
     *
     * @param userId ID của người dùng.
     * @param role   Chức vụ mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeOthersRole(int userId, Role role) {
        if (!isLogInAndConnectedToTheDB("change role")) {
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
        return executeUserQuery("change role", query, role.name(), userId);
    }

    /**
     * Thay đổi trạng thái tài khoản của người dùng (trừ Admin).
     *
     * @param userId ID của người dùng.
     * @param status Trạng thái tài khoản mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changeOthersAccountStatus(int userId, AccountStatus status) {
        if (!isLogInAndConnectedToTheDB("change account status")) {
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
        return executeUserQuery("change account status", query, status.name(), userId);
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
        if (!isLogInAndConnectedToTheDB("change password")) {
            return false;
        }

        if (newPassword.equals(oldPassword)) {
            logger.info("New password is the same");
            return false;
        }

        if (!PasswordEncoder.compare(oldPassword, passwordHash)) {
            logger.info(LogMsg.wrongPW);
            return false;
        }

        passwordHash = PasswordEncoder.encode(newPassword);

        String query = "update user set passwordHash = ? where id = ?";
        return executeUserQuery("change password", query, passwordHash, currentUser.getId());
    }

    /**
     * Thay đổi mật khẩu của tài khoản người dùng khác (trừ Admin).
     *
     * @param userId      ID của người dùng.
     * @param newPassword Mật khẩu mới.
     * @return true nếu thay đổi thành công.
     */
    public static boolean changePassword(int userId, String newPassword) {
        if (!isLogInAndConnectedToTheDB("change password")) {
            return false;
        }

        if (userId == currentUser.getId()) {
            logger.info("Old password is required to change it");
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
            logger.info(LogMsg.userNotAllowTo("change others' password"));
            return false;
        }

        User user = getUserById(userId);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("change other Admin's password"));
            return false;
        }

        String newPasswordHash = PasswordEncoder.encode(newPassword);
        String query = "update user set passwordHash = ? where id = ?";
        return executeUserQuery("change password", query, newPasswordHash, user.getId());
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
     * @return true nếu xoá thành công.
     */
    public static boolean deleteOwnAccount(String password) {
        if (!isLogInAndConnectedToTheDB("delete account")) {
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.DELETE_OWN_ACCOUNT)) {
            logger.info(LogMsg.userNotAllowTo("delete their own account"));
            return false;
        }

        if (!PasswordEncoder.compare(password, passwordHash)) {
            logger.info(LogMsg.wrongPW);
            return false;
        }

        String query = "delete from user where id = ?";
        if (executeUserQuery("delete account", query, currentUser.getId())) {
            currentUser = null;
            passwordHash = "";
            logger.info("Logging out");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Xoá tài khoản (Admin).
     *
     * @param userId ID của tài khoản cần xoá.
     * @return true nếu xoá thành công.
     */
    public static boolean deleteOthersAccount(int userId) {
        if (!isLogInAndConnectedToTheDB("delete account")) {
            return false;
        }

        if (userId == currentUser.getId()) {
            logger.info("Password is required to delete your own password");
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.DELETE_LOWER_ACCOUNT)) {
            logger.info(LogMsg.userNotAllowTo("delete others' account"));
            return false;
        }

        User user = getUserById(userId);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("delete other Admin's account"));
            return false;
        }

        String query = "delete from user where id = ?";
        return executeUserQuery("delete account", query, userId);
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

    private static boolean executeUserQuery(String work, String query, Object... params) {
        try {
            int updates = DBManager.executeUpdate(query, params);
            if (updates >= 1) {
                logger.info(LogMsg.success(work));
                return true;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.error(LogMsg.fail(work), e);
            return false;
        }
    }

    private static boolean isLogInAndConnectedToTheDB(String work) {
        if (!DBManager.isConnected()) {
            logger.info(LogMsg.noDBConnection(work));
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        return true;
    }
}