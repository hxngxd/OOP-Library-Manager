package com.hxngxd.entities;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.Role;
import com.hxngxd.exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User extends Person {
    private static final Logger log = LogManager.getLogger(User.class);
    private String username;
    private String email;
    private String passwordHash;
    private String address;
    private Role role;
    private AccountStatus accountStatus;
    private int violationCount;
    private final List<Book> savedBooks = new ArrayList<>();

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String firstName, String lastName, LocalDate dateOfBirth,
                String username, String email, String passwordHash,
                String address, int violationCount) {
        super(id, firstName, lastName, dateOfBirth);
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.address = address;
        this.violationCount = violationCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(int violationCount) {
        this.violationCount = violationCount;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        return this.id == ((User) other).getId();
    }

    @Override
    public String toString() {
        return "Username: " + this.username + "\n" +
                "ID: " + this.id + "\n" +
                "Role: " + this.role.name() + "\n";
    }

    public List<Book> getSavedBooks() {
        return savedBooks;
    }

    public void saveBook(Book book)
            throws DatabaseException {
        if (!savedBooks.contains(book)) {
            savedBooks.add(book);
            DatabaseManager db = DatabaseManager.getInstance();
            db.insert("userSavedBook", false,
                    List.of("userId", "bookId"),
                    getId(), book.getId());
            log.info(LogMessages.General.SUCCESS.getMessage("save book"));
        } else {
            throw new DatabaseException("This book is already saved");
        }
    }

    public void unsaveBook(Book book)
            throws DatabaseException {
        if (savedBooks.contains(book)) {
            savedBooks.remove(book);
            DatabaseManager db = DatabaseManager.getInstance();
            db.delete("userSavedBook",
                    List.of("userId", "bookId"),
                    getId(), book.getId());
            log.info(LogMessages.General.SUCCESS.getMessage("unsave book"));
        } else {
            throw new DatabaseException("The book is not saved");
        }
    }
}