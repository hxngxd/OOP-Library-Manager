package com.hxngxd.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private static final Logger logger = LogManager.getLogger(InputValidator.class);

    private InputValidator() {
    }

    public static boolean validateInput(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                logger.info(LogMsg.infoIsMissing);
                return false;
            }
            if (input.length() > 127) {
                logger.info(LogMsg.infoTooLong);
                return false;
            }
        }
        return true;
    }

    public static boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
