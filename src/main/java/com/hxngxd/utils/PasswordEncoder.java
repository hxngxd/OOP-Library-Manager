package com.hxngxd.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Lớp PasswordEncoder cung cấp các phương thức để mã hóa và so sánh mật khẩu bằng thuật toán bcrypt.
 * Lớp này sử dụng cost factor là 11 để điều chỉnh số lần lặp trong quá trình mã hóa.
 * <p>
 * Đây là một lớp tiện ích không thể khởi tạo đối tượng trực tiếp, chỉ cung cấp các phương thức tĩnh.
 * <p>
 * Sử dụng:
 * - Phương thức encode(String originalPassword) để mã hóa mật khẩu.
 * - Phương thức compare(String originalPassword, String encodedPassword) để so sánh mật khẩu gốc với mật khẩu đã mã hóa.
 * <p>
 * Sử dụng thư viện jBCrypt để thực hiện mã hóa và so sánh mật khẩu.
 */
public class PasswordEncoder {

    /**
     * cost_factor xác định số lần lặp của thuật toán bcrypt.
     * Giá trị mặc định là 11. Tăng giá trị này sẽ tăng độ phức tạp và bảo mật, nhưng cũng làm tăng thời gian xử lý.
     */
    private static final int cost_factor = 11;

    /**
     * Hàm khởi tạo private để ngăn chặn việc tạo đối tượng từ lớp này.
     * PasswordEncoder chỉ cung cấp các phương thức tĩnh.
     */
    private PasswordEncoder() {
    }

    /**
     * Mã hóa mật khẩu gốc bằng thuật toán bcrypt.
     *
     * @param originalPassword Mật khẩu gốc cần mã hóa.
     * @return Mật khẩu đã được mã hóa dưới dạng chuỗi.
     */
    public static String encode(String originalPassword) {
        String salt = BCrypt.gensalt(cost_factor);
        return BCrypt.hashpw(originalPassword, salt);
    }

    /**
     * So sánh mật khẩu gốc với mật khẩu đã mã hóa.
     * Phương thức này kiểm tra xem mật khẩu gốc khi được mã hóa lại có trùng với mật khẩu đã mã hóa hay không.
     *
     * @param originalPassword Mật khẩu gốc cần kiểm tra.
     * @param encodedPassword Mật khẩu đã mã hóa để so sánh.
     * @return true nếu mật khẩu khớp, false nếu không khớp hoặc có lỗi trong quá trình kiểm tra.
     */
    public static boolean compare(String originalPassword, String encodedPassword) {
        try {
            return BCrypt.checkpw(originalPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}