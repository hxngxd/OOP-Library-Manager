package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.exceptions.PasswordException;
import com.hxngxd.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputHandler {

    private static final Map<Character, Character> vietnameseCharMap = new HashMap<>();

    static {
        vietnameseCharMap.put('à', 'a');
        vietnameseCharMap.put('á', 'a');
        vietnameseCharMap.put('ả', 'a');
        vietnameseCharMap.put('ã', 'a');
        vietnameseCharMap.put('ạ', 'a');
        vietnameseCharMap.put('â', 'a');
        vietnameseCharMap.put('ầ', 'a');
        vietnameseCharMap.put('ấ', 'a');
        vietnameseCharMap.put('ẩ', 'a');
        vietnameseCharMap.put('ẫ', 'a');
        vietnameseCharMap.put('ậ', 'a');
        vietnameseCharMap.put('ă', 'a');
        vietnameseCharMap.put('ằ', 'a');
        vietnameseCharMap.put('ắ', 'a');
        vietnameseCharMap.put('ẳ', 'a');
        vietnameseCharMap.put('ẵ', 'a');
        vietnameseCharMap.put('ặ', 'a');
        vietnameseCharMap.put('è', 'e');
        vietnameseCharMap.put('é', 'e');
        vietnameseCharMap.put('ẻ', 'e');
        vietnameseCharMap.put('ẽ', 'e');
        vietnameseCharMap.put('ẹ', 'e');
        vietnameseCharMap.put('ê', 'e');
        vietnameseCharMap.put('ề', 'e');
        vietnameseCharMap.put('ế', 'e');
        vietnameseCharMap.put('ể', 'e');
        vietnameseCharMap.put('ễ', 'e');
        vietnameseCharMap.put('ệ', 'e');
        vietnameseCharMap.put('ì', 'i');
        vietnameseCharMap.put('í', 'i');
        vietnameseCharMap.put('ỉ', 'i');
        vietnameseCharMap.put('ĩ', 'i');
        vietnameseCharMap.put('ị', 'i');
        vietnameseCharMap.put('ò', 'o');
        vietnameseCharMap.put('ó', 'o');
        vietnameseCharMap.put('ỏ', 'o');
        vietnameseCharMap.put('õ', 'o');
        vietnameseCharMap.put('ọ', 'o');
        vietnameseCharMap.put('ô', 'o');
        vietnameseCharMap.put('ồ', 'o');
        vietnameseCharMap.put('ố', 'o');
        vietnameseCharMap.put('ổ', 'o');
        vietnameseCharMap.put('ỗ', 'o');
        vietnameseCharMap.put('ộ', 'o');
        vietnameseCharMap.put('ơ', 'o');
        vietnameseCharMap.put('ờ', 'o');
        vietnameseCharMap.put('ớ', 'o');
        vietnameseCharMap.put('ở', 'o');
        vietnameseCharMap.put('ỡ', 'o');
        vietnameseCharMap.put('ợ', 'o');
        vietnameseCharMap.put('ù', 'u');
        vietnameseCharMap.put('ú', 'u');
        vietnameseCharMap.put('ủ', 'u');
        vietnameseCharMap.put('ũ', 'u');
        vietnameseCharMap.put('ụ', 'u');
        vietnameseCharMap.put('ư', 'u');
        vietnameseCharMap.put('ừ', 'u');
        vietnameseCharMap.put('ứ', 'u');
        vietnameseCharMap.put('ử', 'u');
        vietnameseCharMap.put('ữ', 'u');
        vietnameseCharMap.put('ự', 'u');
        vietnameseCharMap.put('ỳ', 'y');
        vietnameseCharMap.put('ý', 'y');
        vietnameseCharMap.put('ỷ', 'y');
        vietnameseCharMap.put('ỹ', 'y');
        vietnameseCharMap.put('ỵ', 'y');
        vietnameseCharMap.put('đ', 'd');
    }

    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public static final double similarThresHold = 0.8;

    private InputHandler() {
    }

    public static void validateInput(String... inputs)
            throws ValidationException {
        for (String input : inputs) {
            if (input.isEmpty()) {
                throw new ValidationException(LogMessages.Validation.INFO_IS_MISSING.getMSG());
            }
            if (input.length() > 127) {
                throw new ValidationException(LogMessages.Validation.INFO_TOO_LONG.getMSG());
            }
        }
    }

    public static void validateEmail(String email)
            throws ValidationException {
        Matcher matcher = emailPattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException(LogMessages.Validation.EMAIL_NOT_VALID.getMSG());
        }
    }

    public static void validatePassword(String password)
            throws PasswordException {
        if (password.length() <= 6) {
            throw new PasswordException("Password size should be greater than 6");
        }

        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLowercase && hasUppercase && hasDigit) {
                break;
            }
        }

        if (!hasLowercase) {
            throw new PasswordException("Password should contain at least one lowercase letter");
        }
        if (!hasUppercase) {
            throw new PasswordException("Password should contain at least one uppercase letter");
        }
        if (!hasDigit) {
            throw new PasswordException("Password should contain at least one digit");
        }
    }

    public static int damerauLevenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                    );

                    if (i > 1 && j > 1 && str1.charAt(i - 1) == str2.charAt(j - 2) && str1.charAt(i - 2) == str2.charAt(j - 1)) {
                        dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                    }
                }
            }
        }
        return dp[len1][len2];
    }

    public static double similarity(String info, String search) {
        info = info.toLowerCase().replaceAll("\\s+", "");
        search = search.toLowerCase().replaceAll("\\s+", "");

        int bound = 3;

        if (search.length() <= bound) {
            if (info.length() < search.length()) {
                return 0.0;
            }
            if (info.contains(search)) {
                return 1.0;
            }
            return 0.0;
        }

        if (info.contains(search)) {
            return 1.0;
        }

        double maxSimilarity = 0.0;

        for (int subLength = bound + 1; subLength <= search.length(); subLength++) {
            for (int start = 0; start <= search.length() - subLength; start++) {
                String searchSubStr = search.substring(start, start + subLength);

                for (int len = subLength - 1; len <= subLength + 1; len++) {
                    if (len < 1 || len > info.length()) continue;

                    for (int i = 0; i <= info.length() - len; i++) {
                        String infoSubStr = info.substring(i, i + len);
                        int distance = damerauLevenshteinDistance(infoSubStr, searchSubStr);
                        double similarity = 1.0 - ((double) distance / Math.max(infoSubStr.length(), searchSubStr.length()));
                        maxSimilarity = Math.max(maxSimilarity, similarity);

                        if (maxSimilarity == 1.0) {
                            return maxSimilarity;
                        }
                    }
                }
            }
        }

        return maxSimilarity;
    }

    public static boolean isSimilar(String info, String search) {
        return similarity(info, search) >= similarThresHold;
    }

    public static boolean isUnidecodeSimilar(String info, String search) {
        return similarity(unidecode(info), unidecode(search)) >= similarThresHold;
    }

    public static boolean unidecodedPrefixMatching(String text, String prefix) {
        return exactPrefixMatching(unidecode(text), unidecode(prefix));
    }

    public static boolean lowerPrefixMatching(String text, String prefix) {
        return exactPrefixMatching(text.toLowerCase(), prefix.toLowerCase());
    }

    public static boolean exactPrefixMatching(String text, String prefix) {
        return text.startsWith(prefix);
    }

    public static String unidecode(String text) {
        StringBuilder sb = new StringBuilder();
        text = text.toLowerCase();
        for (char c : text.toCharArray()) {
            sb.append(vietnameseCharMap.getOrDefault(c, c));
        }
        return sb.toString();
    }

}