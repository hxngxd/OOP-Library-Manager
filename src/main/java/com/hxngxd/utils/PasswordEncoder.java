package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.exceptions.PasswordMismatchException;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    private static final int cost_factor = 12;

    private PasswordEncoder() {
    }

    public static String encode(String originalPassword) {
        String salt = BCrypt.gensalt(cost_factor);
        return BCrypt.hashpw(originalPassword, salt);
    }

    public static void compare(String originalPassword, String hashedPassword)
            throws PasswordMismatchException {
        if (!BCrypt.checkpw(originalPassword, hashedPassword)) {
            throw new PasswordMismatchException(LogMessages.User.WRONG_PASSWORD.getMessage());
        }
    }
}