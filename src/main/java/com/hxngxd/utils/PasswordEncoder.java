package com.hxngxd.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    private static final int cost_factor = 11;

    private PasswordEncoder() {
    }

    public static String encode(String originalPassword) {
        String salt = BCrypt.gensalt(cost_factor);
        return BCrypt.hashpw(originalPassword, salt);
    }

    public static boolean compare(String originalPassword, String encodedPassword) {
        try {
            return BCrypt.checkpw(originalPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }

}
