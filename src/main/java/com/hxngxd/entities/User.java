package com.hxngxd.entities;

import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Lớp User kế thừa từ Person, đại diện cho người dùng trong hệ thống.
 */
@Getter
@Setter
public class User extends Person {

    private String username;
    private String email;
    private String address;
    private Role role;
    private AccountStatus accountStatus;
    private int violationCount;

    public User(int id, String firstName, String lastName, LocalDate dateOfBirth, String username, String email, String address, Role role, AccountStatus accountStatus, int violationCount) {
        super(id, firstName, lastName, dateOfBirth);
        this.username = username;
        this.email = email;
        this.address = address;
        this.role = role;
        this.accountStatus = accountStatus;
        this.violationCount = violationCount;
    }
}