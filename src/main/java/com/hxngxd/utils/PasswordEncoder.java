package com.hxngxd.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    private static final int cost_factor = 12;

    private PasswordEncoder() {
    }

    public static String encode(String originalPassword) {
        String salt = BCrypt.gensalt(cost_factor);
        return BCrypt.hashpw(originalPassword, salt);
    }

    public static boolean compare(String originalPassword, String hashedPassword) {
        return BCrypt.checkpw(originalPassword, hashedPassword);
    }
}