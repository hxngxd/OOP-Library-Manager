package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.PasswordException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.utils.InputValidator;
import com.hxngxd.utils.PasswordEncoder;
import com.hxngxd.enums.LogMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private final DatabaseManager db = DatabaseManager.getInstance();
    private static final Logger log = LogManager.getLogger(UserService.class);
    private User currentUser = null;

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

    public static boolean isLoggedIn() {
        return UserService.getInstance().getCurrentUser() != null;
    }

    public static void checkLoggedInAndConnected()
            throws DatabaseException, UserException {
        if (!DatabaseManager.isConnected()) {
            throw new DatabaseException(LogMessages.Database.NO_DB_CONNECTION.getMessage());
        }
        if (!UserService.isLoggedIn()) {
            throw new UserException(LogMessages.User.USER_NOT_LOGGED_IN.getMessage());
        }
    }

    public static void checkLoggedOutAndConnected()
            throws DatabaseException, UserException {
        if (!DatabaseManager.isConnected()) {
            throw new DatabaseException(LogMessages.Database.NO_DB_CONNECTION.getMessage());
        }
        if (UserService.isLoggedIn()) {
            throw new UserException(LogMessages.User.USER_NOT_LOGGED_OUT.getMessage());
        }
    }

    public void register(String firstName, String lastName, String username,
                         String email, String password, String confirmedPassword)
            throws DatabaseException, UserException, ValidationException {
        checkLoggedOutAndConnected();

        InputValidator.validateInput(firstName, lastName, username, email,
                password, confirmedPassword);

        InputValidator.validateEmail(email);

        if (getUserByUsernameOrEmail(false, username, email) != null) {
            throw new UserException(LogMessages.User.USER_EXIST.getMessage());
        }

        InputValidator.validatePassword(password);

        if (!confirmedPassword.equals(password)) {
            throw new PasswordException("Please reconfirm your password");
        }

        String passwordHash = PasswordEncoder.encode(confirmedPassword);

        db.insert("user",
                List.of("firstName", "lastName", "username", "email",
                        "passwordHash", "accountStatus"),
                firstName, lastName, username, email,
                passwordHash, AccountStatus.ACTIVE.name());

        currentUser = new User(db.getGeneratedId());
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        currentUser.setPasswordHash(passwordHash);
        currentUser.setRole(Role.USER);
        currentUser.setAccountStatus(AccountStatus.ACTIVE);
        currentUser.setViolationCount(0);

        log.info(LogMessages.General.SUCCESS.getMessage("create account"));
    }

    public void login(String username, String email, String password)
            throws DatabaseException, UserException, ValidationException {
        checkLoggedOutAndConnected();

        InputValidator.validateInput(username, email, password);

        User user = getUserByUsernameOrEmail(false, username, email);
        if (user == null) {
            throw new UserException(LogMessages.User.USER_NOT_FOUND.getMessage());
        }

        PasswordEncoder.compare(password, user.getPasswordHash());

        if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new UserException(LogMessages.User.USER_SUSPENDED.getMessage());
        }

        if (user.getAccountStatus() == AccountStatus.BANNED) {
            throw new UserException(LogMessages.User.USER_BANNED.getMessage());
        }

        user = getUserByUsernameOrEmail(true, username, email);
        db.update("user",
                "accountStatus", AccountStatus.ACTIVE.name(),
                "id", user.getId());
        currentUser = user;

        log.info(LogMessages.General.SUCCESS.getMessage("log in"));
    }

    public void logout()
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        db.update("user",
                List.of("lastActive", "accountStatus"),
                List.of(Timestamp.valueOf(LocalDateTime.now()), AccountStatus.INACTIVE.name()),
                List.of("id"),
                List.of(currentUser.getId()));
        currentUser = null;

        log.info(LogMessages.General.SUCCESS.getMessage("log out"));
    }

//    public boolean updateProfile(int userId, String newFirstName, String newLastName,
//                                 LocalDate newDateOfBirth, String newAddress) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (userId != currentUser.getId()) {
//            if (!currentUser.getRole().hasPermission(Permission.EDIT_OTHERS_PROFILE)) {
//                log.info(LogMessages.userNotAllowTo("change others' profile"));
//                return false;
//            }
//
//            User user = getUserbyId(false, userId);
//            if (user == null) {
//                log.info(LogMessages.userNotFound);
//                return false;
//            } else {
//                if (user.getRole() == Role.ADMIN) {
//                    log.info(LogMessages.userCant("change other Admins' profile"));
//                    return false;
//                }
//            }
//        }
//
//        if (!InputValidator.validateInput(
//                newFirstName, newLastName, newAddress
//        )) {
//            return false;
//        }
//
//        boolean update = db.update("user",
//                List.of("firstName", "lastName", "dateOfBirth", "address"),
//                List.of(newFirstName, newLastName,
//                        Date.valueOf(newDateOfBirth), newAddress),
//                List.of("id"), List.of(userId));
//        return LogMessages.logResult(update, "update profile");
//    }
//
//    public boolean changeEmail(int userId, String newEmail) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (userId != currentUser.getId()) {
//            if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
//                log.info(LogMessages.userNotAllowTo("change others' email"));
//                return false;
//            }
//
//            User other = getUserbyId(false, userId);
//            if (other == null) {
//                log.info(LogMessages.userNotFound);
//                return false;
//            } else {
//                if (other.getRole() == Role.ADMIN) {
//                    log.info(LogMessages.userCant("change other Admins' email"));
//                    return false;
//                }
//            }
//        } else {
//            if (newEmail.equals(currentUser.getEmail())) {
//                log.info("New email is still the same");
//                return false;
//            }
//        }
//
//        if (!InputValidator.validateEmail(newEmail)) {
//            log.info(LogMessages.emailNotValid);
//            return false;
//        }
//
//        if (getUserByUsernameOrEmail(false, " ", newEmail) != null) {
//            log.info("Email is already used by other users");
//            return false;
//        }
//
//        boolean update = db.update("user", "email", newEmail,
//                "id", userId);
//        return LogMessages.logResult(update, "change email");
//    }
//
//    public boolean changeOthersRole(int userId, Role role) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHERS_ROLE)) {
//            log.info(LogMessages.userNotAllowTo("change others' role"));
//            return false;
//        }
//
//        if (userId == currentUser.getId()) {
//            log.info(LogMessages.userCant("change their own role"));
//            return false;
//        }
//
//        User user = getUserbyId(false, userId);
//        if (user == null) {
//            log.info(LogMessages.userNotFound);
//            return false;
//        }
//
//        if (user.getRole() == Role.ADMIN) {
//            log.info(LogMessages.userCant("change others' Admin role"));
//            return false;
//        }
//
//        boolean update = db.update("user", "role", role.name(),
//                "id", userId);
//        return LogMessages.logResult(update, "change others' role");
//    }
//
//    public boolean changeOthersAccountStatus(int userId, AccountStatus status) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHERS_ACCOUNT_STATUS)) {
//            log.info(LogMessages.userNotAllowTo("change others' account status"));
//            return false;
//        }
//
//        if (status == AccountStatus.ACTIVE || status == AccountStatus.INACTIVE) {
//            log.info(LogMessages.userDontHaveTo);
//            return false;
//        }
//
//        if (userId == currentUser.getId()) {
//            log.info(LogMessages.userCant(
//                    "set their own account status to SUSPENDED or BANNED"));
//            return false;
//        }
//
//        User user = getUserbyId(false, userId);
//        if (user == null) {
//            log.info(LogMessages.userNotFound);
//            return false;
//        }
//
//        if (user.getRole() == Role.ADMIN) {
//            log.info(LogMessages.userCant("change others' Admin account status"));
//            return false;
//        }
//
//        boolean update = db.update("user", "accountStatus", status.name(),
//                "id", userId);
//        return LogMessages.logResult(update, "change others' status");
//    }
//
//    public boolean changePassword(String oldPassword, String newPassword) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (!PasswordEncoder.compare(oldPassword, currentUser.getPasswordHash())) {
//            log.info(LogMessages.wrongPW);
//            return false;
//        }
//
//        boolean update = db.update("user", "passwordHash",
//                PasswordEncoder.encode(currentUser.getPasswordHash()),
//                "id", currentUser.getId());
//        return LogMessages.logResult(update, "change password");
//    }
//
//    public boolean changePassword(int userId, String newPassword) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
//            log.info(LogMessages.userNotAllowTo("change others' password"));
//            return false;
//        }
//
//        User user = getUserbyId(false, userId);
//        if (user == null) {
//            log.info(LogMessages.userNotFound);
//            return false;
//        }
//
//        if (user.getRole() == Role.ADMIN) {
//            log.info(LogMessages.userCant("change other Admin's password"));
//            return false;
//        }
//
//        boolean update = db.update("user", "passwordHash",
//                PasswordEncoder.encode(newPassword), "id", userId);
//        return LogMessages.logResult(update, "change others' password");
//    }
//
////    public static boolean resetPasswordRequest(String email) {
////        return true;
////    }
////
////    public static boolean emailOTPRequest(String email) {
////        return true;
////    }
////
////    public static boolean verifyEmail(String email, String OTP) {
////        return true;
////    }
//
//    public boolean deleteOwnAccount(String password) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (!currentUser.getRole().hasPermission(Permission.DELETE_OWN_ACCOUNT)) {
//            log.info(LogMessages.userNotAllowTo("delete their own account"));
//            return false;
//        }
//
//        if (!PasswordEncoder.compare(password, currentUser.getPasswordHash())) {
//            log.info(LogMessages.wrongPW);
//            return false;
//        }
//
//        boolean delete = db.delete("user", "id", currentUser.getId());
//        if (delete) {
//            currentUser = null;
//        }
//        return LogMessages.logResult(delete, "delete your account and log out");
//    }
//
//    public boolean deleteOthersAccount(int userId) {
//        if (!checkLoggedInAndConnected()) {
//            return false;
//        }
//
//        if (userId == currentUser.getId()) {
//            log.info("Password is required to delete your own password");
//            return false;
//        }
//
//        if (!currentUser.getRole().hasPermission(Permission.DELETE_LOWER_ACCOUNT)) {
//            log.info(LogMessages.userNotAllowTo("delete others' account"));
//            return false;
//        }
//
//        User user = getUserbyId(false, userId);
//        if (user == null) {
//            log.info(LogMessages.userNotFound);
//            return false;
//        }
//
//        if (user.getRole() == Role.ADMIN) {
//            log.info(LogMessages.userCant("delete other Admin's account"));
//            return false;
//        }
//
//        boolean delete = db.delete("user", "id", userId);
//        return LogMessages.logResult(delete, "delete account");
//    }

    private User getUserbyId(Boolean inDetail, int id)
            throws UserException {
        return getUser(inDetail, id, "", "");
    }

    private User getUserByUsernameOrEmail(Boolean inDetail, String username, String email)
            throws UserException {
        return getUser(inDetail, -1, username, email);
    }

    private User getUser(Boolean inDetail, Object... params) {
        String query = "select * from user where id = ? or username = ? or email = ?";
        return db.select("getting user", query, resultSet -> {
            if (resultSet.next()) {
                User user = new User(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("passwordHash"));
                user.setAccountStatus(AccountStatus.valueOf(resultSet.getString("accountStatus")));
                if (inDetail) {
                    user.setFirstName(resultSet.getString("firstName"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setAddress(resultSet.getString("address"));
                    user.setDateOfBirth(resultSet.getDate("dateOfBirth").toLocalDate());
                    user.setRole(Role.valueOf(resultSet.getString("role")));
                    user.setViolationCount(resultSet.getInt("violationCount"));
                }
                return user;
            }
            return null;
        }, params);
    }
}