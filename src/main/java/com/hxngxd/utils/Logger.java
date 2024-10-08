package com.hxngxd.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp Logger là một lớp theo mô hình Singleton để ghi log với các mức độ như INFO, DEBUG, và ERROR.
 * Mỗi thông báo log sẽ bao gồm timestamp (giờ, phút, giây), mức độ của log và tên của lớp gọi đến.
 * Các thông báo được định dạng sao cho dễ đọc và theo dõi.
 */
public class Logger {

    // Định dạng thời gian (giờ, phút, giây) để chèn vào mỗi thông báo log
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Phương thức khởi tạo private để ngăn việc tạo đối tượng Logger từ bên ngoài (theo mô hình Singleton).
     * Chỉ các phương thức tĩnh có thể được sử dụng để ghi log.
     */
    private Logger() {
    }

    /**
     * Ghi log một thông báo với mức độ log và thông báo cụ thể.
     * Các thông báo log sẽ được căn chỉnh theo cột để đảm bảo dễ đọc.
     *
     * @param level   Mức độ của log (INFO, DEBUG, hoặc ERROR).
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo log cần ghi.
     */
    private static void log(String level, Class<?> clazz, String message) {
        String timestamp = LocalTime.now().format(timeFormatter);
        String className = clazz.getSimpleName();
        String logMessage = String.format("[%s] [%s] %-20s: %s", timestamp, level, className, message);
        System.out.println(logMessage);
    }

    /**
     * Ghi log một thông báo ở mức độ INFO.
     * <p>
     * Mức độ INFO được sử dụng để ghi lại các sự kiện chung trong hệ thống, thường để giám sát hoạt động thông thường.
     *
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo cần ghi ở mức độ INFO.
     */
    public static void info(Class<?> clazz, String message) {
        Logger.log("INFO", clazz, message);
    }

    /**
     * Ghi log một thông báo ở mức độ DEBUG.
     * <p>
     * Mức độ DEBUG thường được sử dụng cho mục đích gỡ lỗi, ghi lại các thông tin chi tiết về hệ thống nhằm giúp phát hiện vấn đề.
     *
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo cần ghi ở mức độ DEBUG.
     */
    public static void debug(Class<?> clazz, String message) {
        Logger.log("DEBUG", clazz, message);
    }

    /**
     * Ghi log một thông báo ở mức độ ERROR.
     * <p>
     * Mức độ ERROR thường được sử dụng để ghi lại các lỗi nghiêm trọng trong hệ thống, giúp phát hiện các sự cố quan trọng cần được xử lý.
     *
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo cần ghi ở mức độ ERROR.
     */
    public static void error(Class<?> clazz, String message) {
        Logger.log("ERROR", clazz, message);
    }
}