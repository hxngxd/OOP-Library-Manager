package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.exceptions.PasswordException;
import com.hxngxd.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private static final Logger log = LogManager.getLogger(InputHandler.class);

    public static final int editDistanceThreshHold = 2;

    private InputHandler() {
    }

    public static void validateInput(String... inputs)
            throws ValidationException {
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

    public static void validateEmail(String email)
            throws ValidationException {
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException(
                    LogMessages.Validation.EMAIL_NOT_VALID.getMessage()
            );
        }
    }

    public static void validatePassword(String password)
            throws PasswordException {
        if (password.length() < 6) {
            throw new PasswordException("Password size should not be less than 6");
        }
    }

    public static int levenshteinDistance(String s1, String s2) {
        int lenS1 = s1.length();
        int lenS2 = s2.length();
        int[] prevRow = new int[lenS2 + 1];
        int[] currRow = new int[lenS2 + 1];

        for (int j = 0; j <= lenS2; j++) {
            prevRow[j] = j;
        }

        for (int i = 1; i <= lenS1; i++) {
            currRow[0] = i;
            for (int j = 1; j <= lenS2; j++) {
                if (Character.toLowerCase(s1.charAt(i - 1)) == Character.toLowerCase(s2.charAt(j - 1))) {
                    currRow[j] = prevRow[j - 1];
                } else {
                    currRow[j] = Math.min(
                            Math.min(prevRow[j] + 1,    // deletion
                                    currRow[j - 1] + 1), // insertion
                            prevRow[j - 1] + 1           // substitution
                    );
                }
            }
            int[] temp = prevRow;
            prevRow = currRow;
            currRow = temp;
        }
        return prevRow[lenS2];
    }

    public static int minEditDistance(String text, String pattern) {
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            String substring = text.substring(i, i + pattern.length());
            int distance = levenshteinDistance(substring, pattern);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }
}
