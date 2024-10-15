package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;
import com.hxngxd.utils.EmailValidator;
import com.hxngxd.utils.PasswordEncoder;
import com.hxngxd.utils.LogMsg;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService {

    private final Logger logger = LogManager.getLogger(UserService.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    private User currentUser;

    private UserService() {
    }

    private static class SingletonHolder {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance() {
        return SingletonHolder.instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean register(String firstName, String lastName, String username,
                            String email, LocalDate dateOfBirth, String password,
                            String confirmedPassword) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (isLoggedIn()) {
            logger.info(LogMsg.userNotLogOut);
            return false;
        }

        if (firstName.isEmpty() || lastName.isEmpty() ||
                username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmedPassword.isEmpty()) {
            logger.info("Some information is missing");
            return false;
        }

        if (firstName.length() > 127 || lastName.length() > 127 ||
                username.length() > 127 || email.length() > 127 ||
                password.length() > 127 || confirmedPassword.length() > 127) {
            logger.info(LogMsg.infoTooLong);
            return false;
        }

        if (!EmailValidator.validate(email)) {
            logger.info("The provided email is not valid");
            return false;
        }

        if (getUserByUsernameOrEmail(false, username, email) != null) {
            logger.info(LogMsg.userExist);
            return false;
        }

        if (password.length() < 6) {
            logger.info("Length of password must be more than 6");
            return false;
        }

        if (!confirmedPassword.equals(password)) {
            logger.info("Reconfirm your password");
            return false;
        }

        String passwordHash = PasswordEncoder.encode(confirmedPassword);
        boolean insert = db.insert("user",
                List.of("firstName", "lastName", "dateOfBirth", "username", "email",
                        "passwordHash", "accountStatus"),
                firstName, lastName, Date.valueOf(dateOfBirth), username, email,
                passwordHash, AccountStatus.ACTIVE.name());

        if (insert) {
            currentUser = new User();
            currentUser.setId(db.getGeneratedId());
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setDateOfBirth(dateOfBirth);
            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.setPasswordHash(passwordHash);
            currentUser.setRole(Role.USER);
            currentUser.setAccountStatus(AccountStatus.ACTIVE);
            currentUser.setViolationCount(0);
            logger.info(LogMsg.success("create account"));
            return true;
        } else {
            logger.debug(LogMsg.sthwr("creating account"));
            return false;
        }
    }

    public boolean login(String username, String email, String password) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (isLoggedIn()) {
            logger.info(LogMsg.userNotLogOut);
            return false;
        }

        if (username.isEmpty() || email.isEmpty()) {
            logger.info("Username or email is missing");
            return false;
        }

        if (password.isEmpty()) {
            logger.info("Please enter password");
            return false;
        }

        if (username.length() > 127 || email.length() > 127 || password.length() > 127) {
            logger.info(LogMsg.infoTooLong);
            return false;
        }

        User user = getUserByUsernameOrEmail(false, username, email);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (!PasswordEncoder.compare(password, user.getPasswordHash())) {
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

        user = getUserByUsernameOrEmail(true, username, email);
        boolean update = db.update("user",
                "accountStatus", AccountStatus.ACTIVE.name(),
                "id", user.getId());
        if (update) {
            currentUser = user;
            logger.info(LogMsg.success("log in"));
            return true;
        } else {
            currentUser = null;
            logger.error(LogMsg.fail("log in"));
            return false;
        }
    }

    public boolean logout() {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        boolean update = db.update("user",
                List.of("lastActive", "accountStatus"),
                List.of(Timestamp.valueOf(LocalDateTime.now()), AccountStatus.INACTIVE.name()),
                List.of("id"),
                List.of(currentUser.getId()));
        if (update) {
            currentUser = null;
            logger.info(LogMsg.success("log out"));
            return true;
        } else {
            logger.error(LogMsg.fail("log out"));
            return false;
        }
    }

    public boolean updateProfile(int userId, String newFirstName, String newLastName,
                                 LocalDate newDateOfBirth, String newAddress) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (userId != currentUser.getId()) {
            if (!currentUser.getRole().hasPermission(Permission.EDIT_OTHERS_PROFILE)) {
                logger.info(LogMsg.userNotAllowTo("change others' profile"));
                return false;
            }

            User user = getUserbyId(false, userId);
            if (user == null) {
                logger.info(LogMsg.userNotFound);
                return false;
            } else {
                if (user.getRole() == Role.ADMIN) {
                    logger.info(LogMsg.userCant("change other Admins' profile"));
                    return false;
                }
            }
        }

        if (newFirstName.isEmpty() || newLastName.isEmpty() ||
                newAddress.isEmpty()) {
            logger.info("Some information is missing");
            return false;
        }

        if (newFirstName.length() > 127 || newLastName.length() > 127 ||
                newAddress.length() > 127) {
            logger.info(LogMsg.infoTooLong);
            return false;
        }

        boolean update = db.update("user",
                List.of("firstName", "lastName", "dateOfBirth", "address"),
                List.of(newFirstName, newLastName,
                        Date.valueOf(newDateOfBirth), newAddress),
                List.of("id"), List.of(userId));
        if (update) {
            logger.info(LogMsg.success("update profile"));
            return true;
        } else {
            logger.error(LogMsg.fail("update profile"));
            return false;
        }
    }

    public boolean changeEmail(int userId, String newEmail) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (userId != currentUser.getId()) {
            if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
                logger.info(LogMsg.userNotAllowTo("change others' email"));
                return false;
            }

            User other = getUserbyId(false, userId);
            if (other == null) {
                logger.info(LogMsg.userNotFound);
                return false;
            } else {
                if (other.getRole() == Role.ADMIN) {
                    logger.info(LogMsg.userCant("change other Admins' email"));
                    return false;
                }
            }
        } else {
            if (newEmail.equals(currentUser.getEmail())) {
                logger.info("New email is still the same");
                return false;
            }
        }

        if (!EmailValidator.validate(newEmail)) {
            logger.info("Email is not valid");
            return false;
        }

        if (getUserByUsernameOrEmail(false, " ", newEmail) != null) {
            logger.info("Email is already used by other users");
            return false;
        }

        boolean update = db.update("user", "email", newEmail,
                "id", userId);
        if (update) {
            logger.info(LogMsg.success("change email"));
            return true;
        } else {
            logger.error(LogMsg.fail("change email"));
            return false;
        }
    }

    public boolean changeOthersRole(int userId, Role role) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
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

        User user = getUserbyId(false, userId);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("change others' Admin role"));
            return false;
        }

        boolean update = db.update("user", "role", role.name(),
                "id", userId);
        if (update) {
            logger.info(LogMsg.success("change others' role"));
            return true;
        } else {
            logger.error(LogMsg.fail("change others' role"));
            return false;
        }
    }

    public boolean changeOthersAccountStatus(int userId, AccountStatus status) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
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
            logger.info(LogMsg.userCant(
                    "set their own account status to SUSPENDED or BANNED"));
            return false;
        }

        User user = getUserbyId(false, userId);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("change others' Admin account status"));
            return false;
        }

        boolean update = db.update("user", "accountStatus", status.name(),
                "id", userId);
        if (update) {
            logger.info(LogMsg.success("change others' status"));
            return true;
        } else {
            logger.error(LogMsg.fail("change others' status"));
            return false;
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (!PasswordEncoder.compare(oldPassword, currentUser.getPasswordHash())) {
            logger.info(LogMsg.wrongPW);
            return false;
        }

        boolean update = db.update("user", "passwordHash",
                PasswordEncoder.encode(currentUser.getPasswordHash()),
                "id", currentUser.getId());
        if (update) {
            logger.info(LogMsg.success("change password"));
            return true;
        } else {
            logger.error(LogMsg.fail("change password"));
            return false;
        }
    }

    public boolean changePassword(int userId, String newPassword) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
            logger.info(LogMsg.userNotAllowTo("change others' password"));
            return false;
        }

        User user = getUserbyId(false, userId);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("change other Admin's password"));
            return false;
        }

        boolean update = db.update("user", "passwordHash",
                PasswordEncoder.encode(newPassword), "id", userId);
        if (update) {
            logger.info(LogMsg.success("change others' password"));
            return true;
        } else {
            logger.error(LogMsg.fail("change others' password"));
            return false;
        }
    }

//    public static boolean resetPasswordRequest(String email) {
//        return true;
//    }
//
//    public static boolean emailOTPRequest(String email) {
//        return true;
//    }
//
//    public static boolean verifyEmail(String email, String OTP) {
//        return true;
//    }

    public boolean deleteOwnAccount(String password) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
            return false;
        }

        if (!currentUser.getRole().hasPermission(Permission.DELETE_OWN_ACCOUNT)) {
            logger.info(LogMsg.userNotAllowTo("delete their own account"));
            return false;
        }

        if (!PasswordEncoder.compare(password, currentUser.getPasswordHash())) {
            logger.info(LogMsg.wrongPW);
            return false;
        }

        boolean delete = db.delete("user", "id", currentUser.getId());
        if (delete) {
            currentUser = null;
            logger.info(LogMsg.success("delete your account"));
            logger.info("Logging out");
            return true;
        } else {
            logger.error(LogMsg.fail("delete your account"));
            return false;
        }
    }

    public boolean deleteOthersAccount(int userId) {
        if (!db.isConnected()) {
            logger.info(LogMsg.noDBConnection);
            return false;
        }

        if (!isLoggedIn()) {
            logger.info(LogMsg.userNotLogIn);
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

        User user = getUserbyId(false, userId);
        if (user == null) {
            logger.info(LogMsg.userNotFound);
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            logger.info(LogMsg.userCant("delete other Admin's account"));
            return false;
        }

        boolean delete = db.delete("user", "id", userId);
        if (delete) {
            logger.info(LogMsg.success("delete account"));
            return true;
        } else {
            logger.error(LogMsg.fail("delete account"));
            return false;
        }
    }

    private User getUserbyId(Boolean inDetail, int id) {
        return getUser(inDetail, id, "", "");
    }

    private User getUserByUsernameOrEmail(Boolean inDetail, String username, String email) {
        return getUser(inDetail, -1, username, email);
    }

    private User getUser(Boolean inDetail, Object... params) {
        String query = "select * from user where id = ? or username = ? or email = ?";
        try (PreparedStatement pStatement = db.getConnection().prepareStatement(query)) {
            db.setParameters(pStatement, params);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPasswordHash(resultSet.getString("passwordHash"));
                    user.setAccountStatus(
                            AccountStatus.valueOf(
                                    resultSet.getString("accountStatus")));
                    if (inDetail) {
                        user.setFirstName(resultSet.getString("firstName"));
                        user.setLastName(resultSet.getString("lastName"));
                        user.setAddress(resultSet.getString("address"));
                        user.setDateOfBirth(
                                resultSet.getDate("dateOfBirth").toLocalDate());
                        user.setRole(Role.valueOf(resultSet.getString("role")));
                        user.setViolationCount(resultSet.getInt("violationCount"));
                    }
                    return user;
                }
            }
        } catch (SQLException | NullPointerException e) {
            logger.error(LogMsg.sthwr("getting user"), e);
        }
        return null;
    }
}