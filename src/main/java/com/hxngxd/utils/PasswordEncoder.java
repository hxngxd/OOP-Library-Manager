package com.hxngxd.utils;

import com.hxngxd.enums.LogMsg;
import com.hxngxd.exceptions.PasswordException;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordEncoder {

    private static final int cost_factor = 12;

    private PasswordEncoder() {
    }

    public static String encode(String originalPassword) {
        String salt = BCrypt.gensalt(cost_factor);
        return BCrypt.hashpw(originalPassword, salt);
    }

    public static void compare(String originalPassword, String hashedPassword)
            throws PasswordException {
        if (!BCrypt.checkpw(originalPassword, hashedPassword)) {
            throw new PasswordException(LogMsg.USER_WRONG_PASSWORD.msg());
        }
    }

}