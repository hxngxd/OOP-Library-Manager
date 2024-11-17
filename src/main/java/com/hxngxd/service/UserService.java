package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.PasswordException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.utils.ImageHandler;
import com.hxngxd.utils.InputHandler;
import com.hxngxd.utils.PasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class UserService {

    private static final Logger log = LogManager.getLogger(UserService.class);

    private final DatabaseManager db = DatabaseManager.getInstance();

    public static final List<User> userList = new ArrayList<>();

    public static final HashMap<Integer, User> userMap = new HashMap<>();

    private User currentUser = null;

    private UserService() {
    }

    public static void initialize()
            throws DatabaseException, UserException {
        UserService.getInstance().loadSavedBooks();
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
            throw new DatabaseException(LogMessages.Database.NO_DB_CONNECTION.getMSG());
        }
        if (!UserService.isLoggedIn()) {
            throw new UserException(LogMessages.User.USER_NOT_LOGGED_IN.getMSG());
        }
    }

    public static void checkLoggedOutAndConnected()
            throws DatabaseException, UserException {
        if (!DatabaseManager.isConnected()) {
            throw new DatabaseException(LogMessages.Database.NO_DB_CONNECTION.getMSG());
        }
        if (UserService.isLoggedIn()) {
            throw new UserException(LogMessages.User.USER_NOT_LOGGED_OUT.getMSG());
        }
    }

    public void register(String firstName, String lastName, String username,
                         String email, String password, String confirmedPassword)
            throws DatabaseException, UserException, ValidationException {
        checkLoggedOutAndConnected();

        InputHandler.validateInput(firstName, lastName, username, email,
                password, confirmedPassword);

        InputHandler.validateEmail(email);

        if (getUserByUsernameOrEmail(false, username, email) != null) {
            throw new UserException(LogMessages.User.USER_EXIST.getMSG());
        }

        InputHandler.validatePassword(password);

        if (!confirmedPassword.equals(password)) {
            throw new PasswordException("Please reconfirm your password");
        }

        String passwordHash = PasswordEncoder.encode(confirmedPassword);

        db.insert("user", true,
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

        log.info(LogMessages.General.SUCCESS.getMSG("create account"));
    }

    public void login(String username, String email, String password)
            throws DatabaseException, UserException, ValidationException {
        checkLoggedOutAndConnected();

        InputHandler.validateInput(username, email, password);

        User user = getUserByUsernameOrEmail(false, username, email);
        if (user == null) {
            throw new UserException(LogMessages.User.USER_NOT_FOUND.getMSG());
        }

        PasswordEncoder.compare(password, user.getPasswordHash());

        if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new UserException(LogMessages.User.USER_SUSPENDED.getMSG());
        }

        if (user.getAccountStatus() == AccountStatus.BANNED) {
            throw new UserException(LogMessages.User.USER_BANNED.getMSG());
        }

        user = getUserByUsernameOrEmail(true, username, email);
        db.update("user", "accountStatus", AccountStatus.ACTIVE.name(), "id", user.getId());
        currentUser = user;

        currentUser.setAccountStatus(AccountStatus.ACTIVE);

        log.info(LogMessages.General.SUCCESS.getMSG("log in"));
    }

    public void logout()
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        db.update("user",
                List.of("lastActive", "accountStatus"),
                List.of(Timestamp.valueOf(LocalDateTime.now()), AccountStatus.INACTIVE.name()),
                List.of("id"),
                List.of(currentUser.getId()));
        currentUser.setAccountStatus(AccountStatus.INACTIVE);
        currentUser = null;
        userList.clear();
        userMap.clear();

        log.info(LogMessages.General.SUCCESS.getMSG("log out"));
    }

    public void updateProfilePicture(File imageFile)
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        if (imageFile == null) {
            log.info(LogMessages.File.FILE_IS_NULL.getMSG());
            return;
        }

        byte[] toByte = ImageHandler.fileToByteArray(imageFile);

        db.update("user", "photo", toByte, "id", currentUser.getId());

        currentUser.setImage(ImageHandler.loadImageFromFile(imageFile));

        log.info(LogMessages.General.SUCCESS.getMSG("change profile picture"));
    }

    public void updateProfile(String newFirstName, String newLastName,
                              LocalDate newDateOfBirth, String newAddress)
            throws DatabaseException, UserException, ValidationException {
        checkLoggedInAndConnected();

        InputHandler.validateInput(newFirstName, newLastName, newAddress);

        db.update("user",
                List.of("firstName", "lastName", "dateOfBirth", "address"),
                List.of(newFirstName, newLastName, Date.valueOf(newDateOfBirth), newAddress),
                List.of("id"), List.of(currentUser.getId()));

        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setDateOfBirth(newDateOfBirth);
        currentUser.setAddress(newAddress);

        log.info(LogMessages.General.SUCCESS.getMSG("update profile"));
    }

    public void changeEmail(String newEmail)
            throws DatabaseException, UserException, ValidationException {
        checkLoggedInAndConnected();

        InputHandler.validateEmail(newEmail);

        if (getUserByUsernameOrEmail(false, " ", newEmail) != null) {
            throw new UserException(LogMessages.User.USER_EXIST.getMSG());
        }

        db.update("user", "email", newEmail, "id", currentUser.getId());

        currentUser.setEmail(newEmail);

        log.info(LogMessages.General.SUCCESS.getMSG("change email"));
    }

    public void changeRole(int userId, Role role)
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHERS_ROLE)) {
            throw new UserException(LogMessages.User.USER_NOT_ALLOWED.getMSG("change others' role"));
        }

        User user = getUserbyId(false, userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UserException(LogMessages.User.USER_CANNOT.getMSG("change others' Admin role"));
        }

        db.update("user", "role", role.name(), "id", userId);

        log.info(LogMessages.General.SUCCESS.getMSG("change role"));
    }

    public void changeAccountStatus(int userId, AccountStatus status)
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHERS_ACCOUNT_STATUS)) {
            throw new UserException(LogMessages.User.USER_NOT_ALLOWED.getMSG("change others' account status"));
        }

        if (userId == currentUser.getId()) {
            throw new UserException(LogMessages.User.USER_CANNOT.getMSG("set their own account status to SUSPENDED or BANNED"));
        }

        User user = getUserbyId(false, userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UserException(LogMessages.User.USER_CANNOT.getMSG("change others' Admin account status"));
        }

        db.update("user", "accountStatus", status.name(), "id", userId);

        log.info(LogMessages.General.SUCCESS.getMSG("change account status"));
    }

    public void changePassword(String oldPassword, String newPassword)
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        InputHandler.validatePassword(newPassword);

        PasswordEncoder.compare(oldPassword, currentUser.getPasswordHash());

        String newHashedPassword = PasswordEncoder.encode(newPassword);
        db.update("user", "passwordHash", newHashedPassword, "id", currentUser.getId());
        currentUser.setPasswordHash(newHashedPassword);

        log.info(LogMessages.General.SUCCESS.getMSG("change own password"));
    }

    public void changePassword(int userId, String newPassword)
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        if (!currentUser.getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
            throw new UserException(LogMessages.User.USER_NOT_ALLOWED.getMSG("change others' password"));
        }

        User user = getUserbyId(false, userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UserException(LogMessages.User.USER_CANNOT.getMSG("change others' Admin password"));
        }

        InputHandler.validatePassword(newPassword);

        db.update("user", "passwordHash", PasswordEncoder.encode(newPassword), "id", userId);

        log.info(LogMessages.General.SUCCESS.getMSG("change password"));
    }

    public void deleteAccount(int userId)
            throws DatabaseException, UserException {
        checkLoggedInAndConnected();

        if (userId == currentUser.getId()) {
            throw new UserException("Password is required to delete your own account");
        }

        if (!currentUser.getRole().hasPermission(Permission.DELETE_LOWER_ACCOUNT)) {
            throw new UserException(LogMessages.User.USER_NOT_ALLOWED.getMSG("delete others' account"));
        }

        getUserbyId(false, userId);

        db.delete("user", "id", userId);

        log.info(LogMessages.General.SUCCESS.getMSG("delete account"));
    }

    private User getUserbyId(Boolean inDetail, int id)
            throws DatabaseException, UserException {
        User user = getUser(inDetail, id, "", "");
        if (user == null) {
            throw new UserException(LogMessages.User.USER_EXIST.getMSG());
        }
        return user;
    }

    private User getUserByUsernameOrEmail(Boolean inDetail, String username, String email) {
        return getUser(inDetail, -1, username, email);
    }

    private User getUser(Boolean inDetail, Object... params)
            throws DatabaseException, UserException {
        String query = "select * from user where id = ? or username = ? or email = ?";
        return db.select("getting user", query, resultSet -> {
            if (resultSet.next()) {
                return loadUserInformation(inDetail, resultSet);
            }
            return null;
        }, params);
    }

    public void getAllUsers()
            throws DatabaseException {
        userList.clear();
        userMap.clear();

        String query = "select * from user";
        db.select("getting user", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (currentUser.getId() != id) {
                    User user = loadUserInformation(true, resultSet);
                    userList.add(user);
                    userMap.put(id, user);
                } else {
                    userMap.put(currentUser.getId(), currentUser);
                }
            }
            return null;
        });
    }

    private User loadUserInformation(Boolean inDetail, ResultSet rs)
            throws SQLException {
        User user = new User(rs.getInt("id"));

        user.setUsername(rs.getString("username"));

        user.setEmail(rs.getString("email"));

        user.setPasswordHash(rs.getString("passwordHash"));

        user.setAccountStatus(AccountStatus.valueOf(rs.getString("accountStatus")));

        if (!inDetail) {
            return user;
        }

        user.setFirstName(rs.getString("firstName"));

        user.setLastName(rs.getString("lastName"));

        String address = rs.getString("address");
        if (address != null) {
            user.setAddress(address);
        }

        Date dateOfBirth = rs.getDate("dateOfBirth");
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        Timestamp dateAdded = rs.getTimestamp("dateAdded");
        if (dateAdded != null) {
            user.setDateAdded(dateAdded.toLocalDateTime());
        }

        Timestamp lastActive = rs.getTimestamp("lastActive");
        if (lastActive != null) {
            user.setLastUpdated(lastActive.toLocalDateTime());
        }

        user.setRole(Role.valueOf(rs.getString("role")));

        byte[] photoBytes = rs.getBytes("photo");
        if (photoBytes != null) {
            user.setImage(ImageHandler.byteArrayToImage(photoBytes));
        } else {
            user.setImage(ImageHandler.loadImageFromResource("profileImage.jpg"));
        }

        return user;
    }

    private void loadSavedBooks()
            throws DatabaseException, UserException {
        this.currentUser.getSavedBooks().clear();
        String query = "select * from userSavedBook where userId = ?";
        db.select("get saved books", query, resultSet -> {
            while (resultSet.next()) {
                this.currentUser.getSavedBooks().add(BookService.bookMap.get(resultSet.getInt("bookId")));
            }
            return null;
        }, this.currentUser.getId());
    }

}