package com.hxngxd.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lớp EmailValidator cung cấp một phương thức tiện ích để kiểm tra
 * tính hợp lệ của địa chỉ email bằng cách sử dụng biểu thức chính quy.
 * Lớp này đảm bảo địa chỉ email tuân theo các định dạng thường được chấp nhận cho email.
 */
public class EmailValidator {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private EmailValidator() {
    }

    /**
     * Kiểm tra tính hợp lệ của địa chỉ email dựa trên biểu thức chính quy đã định nghĩa.
     * <p>
     * Phương thức này kiểm tra xem địa chỉ email được cung cấp có tuân theo định dạng sau không:
     * - Bắt đầu với các ký tự chữ và số hoặc các ký tự đặc biệt như +, &, *, v.v.
     * - Tiếp theo là ký tự @.
     * - Sau đó là tên miền (ví dụ: example.com).
     * <p>
     * Ví dụ các email hợp lệ:
     * - test@example.com
     * - user.name+tag+sorting@example.com
     * <p>
     * Ví dụ các email không hợp lệ:
     * - invalid-email.com
     * - @missinguser.com
     * - user@.missingtld
     *
     * @param email địa chỉ email cần được kiểm tra tính hợp lệ
     * @return {@code true} nếu email hợp lệ, {@code false} nếu không hợp lệ
     */
    public static boolean validate(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

