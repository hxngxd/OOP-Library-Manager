package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.exceptions.ValidationException;
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

    public static void validateInput(String... inputs) throws ValidationException {
        for (String input : inputs) {
            if (input.isEmpty()) {
                throw new ValidationException(
                        LogMessages.Validation.INFO_IS_MISSING.getMessage()
                );
            }
            if (input.length() > 127) {
                throw new ValidationException(
                        LogMessages.Validation.INFO_TOO_LONG.getMessage()
                );
            }
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException(
                    LogMessages.Validation.EMAIL_NOT_VALID.getMessage()
            );
        }
    }
}
