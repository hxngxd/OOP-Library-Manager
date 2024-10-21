package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private static final Logger log = LogManager.getLogger(InputValidator.class);

    private InputValidator() {
    }

    public static boolean validateInput(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                log.info(LogMessages.Validation.INFO_IS_MISSING.getMessage());
                return false;
            }
            if (input.length() > 127) {
                log.info(LogMessages.Validation.INFO_TOO_LONG.getMessage());
                return false;
            }
        }
        return true;
    }

    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            log.info(LogMessages.Validation.EMAIL_NOT_VALID.getMessage());
            return false;
        }
    }
}
