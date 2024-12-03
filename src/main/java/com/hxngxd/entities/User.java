package com.hxngxd.entities;

import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class User extends Person {

    private String username;
    private String email;
    private String passwordHash;
    private String address;
    private Role role;
    private AccountStatus accountStatus;

    private final Set<Book> savedBooks = new HashSet<>();

    public static final Set<User> userSet = new HashSet<>();
    public static final HashMap<Integer, User> userMap = new HashMap<>();
    private static User current = null;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String firstName, String lastName, LocalDate dateOfBirth,
                String username, String email, String passwordHash,
                String address) {
        super(id, firstName, lastName, dateOfBirth);
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.address = address;
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

    @Override
    public String toString() {
        return "Username: " + this.username + "\n" +
                "ID: " + this.id + "\n" +
                "Role: " + this.role.name() + "\n";
    }

    public void addSavedBook(Book book) {
        savedBooks.add(book);
    }

    public Set<Book> getSavedBooks() {
        return savedBooks;
    }

    public static User getCurrent() {
        return current;
    }

    public static void setCurrent(User current) {
        User.current = current;
    }

    public static void clearCurrent() {
        User.current = null;
    }

}