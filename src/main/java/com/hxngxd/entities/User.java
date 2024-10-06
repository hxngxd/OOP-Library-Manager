package com.hxngxd.entities;

import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;

import java.time.LocalDateTime;

/**
 * Lớp User kế thừa từ Person, đại diện cho người dùng trong hệ thống.
 */
public class User extends Person {

    private String username;
    private String email;
    private String address;
    private Role role;
    private AccountStatus accountStatus;
    private int violationCount;
    private LocalDateTime lastActive;

}