package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.PasswordException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.utils.ImageHandler;
import com.hxngxd.utils.InputHandler;
import com.hxngxd.utils.PasswordEncoder;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class UserService extends Service<User> {

    private UserService() {
    }

    private static class SingletonHolder {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance() {
        return SingletonHolder.instance;
    }

    public void isLoggedIn()
            throws DatabaseException, UserException {
        if (!DatabaseManager.isConnected()) {
            throw new DatabaseException(LogMsg.DATABASE_NO_CONNECTION.msg());
        }
        if (User.getCurrent() == null) {
            throw new UserException(LogMsg.USER_NOT_LOGGED_IN.msg());
        }
    }

    public void isLoggedOut()
            throws DatabaseException, UserException {
        if (!DatabaseManager.isConnected()) {
            throw new DatabaseException(LogMsg.DATABASE_NO_CONNECTION.msg());
        }
        if (User.getCurrent() != null) {
            throw new UserException(LogMsg.USER_NOT_LOGGED_OUT.msg());
        }
    }

    public void register(String firstName, String lastName, String username,
                         String email, String password, String confirmedPassword)
            throws DatabaseException, UserException, ValidationException {
        isLoggedOut();

        InputHandler.validateInput(firstName, lastName, username, email,
                password, confirmedPassword);

        InputHandler.validateEmail(email);

        userNotExist(username, email);

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

        User user = new User(db.getGeneratedId());
        User.setCurrent(user);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(Role.USER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setImage(ImageHandler.loadImageFromResource("profileImage.jpg"));

        log.info(LogMsg.GENERAL_SUCCESS.msg("create account"));
    }

    public void login(String username, String email, String password)
            throws DatabaseException, UserException, ValidationException {
        isLoggedOut();

        InputHandler.validateInput(username, email, password);

        User user = userExist(username, email);

        PasswordEncoder.compare(password, user.getPasswordHash());

        if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new UserException(LogMsg.USER_SUSPENDED.msg());
        }

        if (user.getAccountStatus() == AccountStatus.BANNED) {
            throw new UserException(LogMsg.USER_BANNED.msg());
        }

        user = getUser(true, username, email);
        db.update("user", "accountStatus", AccountStatus.ACTIVE.name(), "id", user.getId());
        User.setCurrent(user);

        User.getCurrent().setAccountStatus(AccountStatus.ACTIVE);

        log.info(LogMsg.GENERAL_SUCCESS.msg("log in"));
    }

    public void logout()
            throws DatabaseException, UserException {
        isLoggedIn();

        db.update("user",
                List.of("lastActive", "accountStatus"),
                List.of(Timestamp.valueOf(LocalDateTime.now()), AccountStatus.INACTIVE.name()),
                List.of("id"),
                List.of(User.getCurrent().getId()));
        User.clearCurrent();

        User.userSet.clear();
        User.userMap.clear();

        log.info(LogMsg.GENERAL_SUCCESS.msg("log out"));
    }

    public void updateProfilePicture(File imageFile)
            throws DatabaseException, UserException {
        isLoggedIn();

        if (imageFile == null) {
            log.info(LogMsg.FILE_IS_NULL.msg());
            return;
        }

        byte[] toByte = ImageHandler.fileToByteArray(imageFile);

        db.update("user", "photo", toByte, "id", User.getCurrent().getId());

        User.getCurrent().setImage(ImageHandler.loadImageFromFile(imageFile));

        log.info(LogMsg.GENERAL_SUCCESS.msg("change profile picture"));
    }

    public void updateProfile(String newFirstName, String newLastName,
                              LocalDate newDateOfBirth, String newAddress)
            throws DatabaseException, UserException, ValidationException {
        isLoggedIn();

        InputHandler.validateInput(newFirstName, newLastName, newAddress);

        db.update("user",
                List.of("firstName", "lastName", "dateOfBirth", "address"),
                List.of(newFirstName, newLastName, Date.valueOf(newDateOfBirth), newAddress),
                List.of("id"), List.of(User.getCurrent().getId()));

        User.getCurrent().setFirstName(newFirstName);
        User.getCurrent().setLastName(newLastName);
        User.getCurrent().setDateOfBirth(newDateOfBirth);
        User.getCurrent().setAddress(newAddress);

        log.info(LogMsg.GENERAL_SUCCESS.msg("update profile"));
    }

    public void changeEmail(String newEmail)
            throws DatabaseException, UserException, ValidationException {
        isLoggedIn();

        InputHandler.validateEmail(newEmail);

        userNotExist("", newEmail);

        db.update("user", "email", newEmail, "id", User.getCurrent().getId());

        User.getCurrent().setEmail(newEmail);

        log.info(LogMsg.GENERAL_SUCCESS.msg("change email"));
    }

    public void changeRole(int userId, Role role)
            throws DatabaseException, UserException {
        isLoggedIn();

        if (!User.getCurrent().getRole().hasPermission(Permission.CHANGE_OTHERS_ROLE)) {
            throw new UserException(LogMsg.USER_NOT_ALLOWED.msg("change others' role"));
        }

        User user = userExist(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UserException(LogMsg.USER_CANNOT.msg("change others' Admin role"));
        }

        db.update("user", "role", role.name(), "id", userId);

        log.info(LogMsg.GENERAL_SUCCESS.msg("change role"));
    }

    public void changeAccountStatus(int userId, AccountStatus status)
            throws DatabaseException, UserException {
        isLoggedIn();

        if (!User.getCurrent().getRole().hasPermission(Permission.CHANGE_OTHERS_ACCOUNT_STATUS)) {
            throw new UserException(LogMsg.USER_NOT_ALLOWED.msg("change others' account status"));
        }

        if (userId == User.getCurrent().getId()) {
            throw new UserException(LogMsg.USER_CANNOT.msg("set their own account status to SUSPENDED or BANNED"));
        }

        User user = userExist(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UserException(LogMsg.USER_CANNOT.msg("change others' Admin account status"));
        }

        db.update("user", "accountStatus", status.name(), "id", userId);

        log.info(LogMsg.GENERAL_SUCCESS.msg("change account status"));
    }

    public void changePassword(String oldPassword, String newPassword)
            throws DatabaseException, UserException {
        isLoggedIn();

        InputHandler.validatePassword(newPassword);

        PasswordEncoder.compare(oldPassword, User.getCurrent().getPasswordHash());

        String newHashedPassword = PasswordEncoder.encode(newPassword);
        db.update("user", "passwordHash", newHashedPassword, "id", User.getCurrent().getId());
        User.getCurrent().setPasswordHash(newHashedPassword);

        log.info(LogMsg.GENERAL_SUCCESS.msg("change own password"));
    }

    public void changePassword(int userId, String newPassword)
            throws DatabaseException, UserException {
        isLoggedIn();

        if (!User.getCurrent().getRole().hasPermission(Permission.CHANGE_OTHER_PASSWORD_EMAIL)) {
            throw new UserException(LogMsg.USER_NOT_ALLOWED.msg("change others' password"));
        }

        User user = userExist(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UserException(LogMsg.USER_CANNOT.msg("change others' Admin password"));
        }

        InputHandler.validatePassword(newPassword);

        db.update("user", "passwordHash", PasswordEncoder.encode(newPassword), "id", userId);

        log.info(LogMsg.GENERAL_SUCCESS.msg("change password"));
    }

    public void deleteAccount(int userId)
            throws DatabaseException, UserException {
        isLoggedIn();

        if (!User.getCurrent().getRole().hasPermission(Permission.DELETE_LOWER_ACCOUNT)) {
            throw new UserException(LogMsg.USER_NOT_ALLOWED.msg("delete others' account"));
        }

        userExist(userId);

        db.delete("user", "id", userId);

        log.info(LogMsg.GENERAL_SUCCESS.msg("delete account"));
    }

    private User userExist(Object... identifiers)
            throws DatabaseException, UserException {
        User user = getUser(false, identifiers);
        if (user == null) {
            throw new UserException(LogMsg.USER_NOT_FOUND.msg());
        }
        return user;
    }

    private void userNotExist(Object... identifiers)
            throws DatabaseException, UserException {
        try {
            userExist(identifiers);
            throw new UserException(LogMsg.USER_EXIST.msg());
        } catch (UserException e) {
            return;
        }
    }

    private User getUser(Boolean inDetail, Object... identifiers)
            throws DatabaseException, UserException {
        String query;
        Object[] params;
        if (identifiers.length == 1 && identifiers[0] instanceof Integer) {
            query = "select * from user where id = ?";
            params = new Object[]{identifiers[0]};
        } else if (identifiers.length == 2 && identifiers[0] instanceof String && identifiers[1] instanceof String) {
            query = "select * from user where username = ? or email = ?";
            params = new Object[]{identifiers[0], identifiers[1]};
        } else {
            throw new IllegalArgumentException("Identifiers must be either an Integer id or a pair of Strings (username, email)");
        }

        return db.select("getting user", query, resultSet -> {
            if (resultSet.next()) {
                return loadUserInformation(inDetail, resultSet);
            }
            return null;
        }, params);
    }

    @Override
    public void loadAll()
            throws DatabaseException {
        User.userSet.clear();
        User.userMap.clear();
        User user = User.getCurrent();

        String query = "select * from user";
        db.select("getting user", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (user.getId() != id) {
                    User.userMap.put(id, loadUserInformation(true, resultSet));
                } else {
                    User.userMap.put(user.getId(), user);
                }
            }
            return null;
        });
        User.userSet.addAll(User.userMap.values());
        User.userSet.remove(user);
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

    public void loadSavedBooks(User user)
            throws DatabaseException, UserException {
        user.getSavedBooks().clear();
        String query = "select * from userSavedBook where userId = ?";
        db.select("get saved books", query, resultSet -> {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("bookId");
                if (Book.bookMap.containsKey(bookId)) {
                    user.addSavedBook(Book.bookMap.get(bookId));
                }
            }
            return null;
        }, user.getId());
    }

    public void toggleSaveBook(Book book, boolean isSave)
            throws DatabaseException {
        User user = User.getCurrent();
        if (isSave) {
            if (!user.getSavedBooks().contains(book)) {
                user.getSavedBooks().add(book);
                db.insert("userSavedBook", false, List.of("userId", "bookId"), user.getId(), book.getId());
                log.info(LogMsg.GENERAL_SUCCESS.msg("save book"));
            } else {
                throw new DatabaseException("This book is already saved");
            }
            return;
        }

        if (user.getSavedBooks().contains(book)) {
            user.getSavedBooks().remove(book);
            db.delete("userSavedBook", List.of("userId", "bookId"), user.getId(), book.getId());
            log.info(LogMsg.GENERAL_SUCCESS.msg("unsave book"));
        } else {
            throw new DatabaseException("The book is not saved");
        }
    }

}