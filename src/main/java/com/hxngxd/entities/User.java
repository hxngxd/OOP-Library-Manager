package com.hxngxd.entities;

import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;

import java.time.LocalDate;

/**
 * Lớp User kế thừa từ Person, đại diện cho người dùng trong hệ thống.
 */
public class User extends Person {
    private String username;
    private String email;
    private String passwordHash;
    private String address;
    private Role role;
    private AccountStatus accountStatus;
    private int violationCount;

    public User() {
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
}