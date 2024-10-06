package com.hxngxd.entities;

import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;
import java.time.LocalDateTime;

public class User extends Person {
    private String username;
    private String email;
    private String address;
    private Role role;
    private AccountStatus accountStatus;
    private int violationCount;
    private LocalDateTime lastActive;
}
