package com.hxngxd.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger là một lớp theo mô hình Singleton để ghi log với các mức độ như INFO, DEBUG, và ERROR.
 * Mỗi thông báo log sẽ bao gồm timestamp (giờ, phút, giây), mức độ của log và tên của class gọi đến.
 */
public class Logger {

    private final DateTimeFormatter timeFormatter;

    /**
     * Phương thức khởi tạo private để ngăn tạo đối tượng từ bên ngoài (Singleton pattern).
     */
    private Logger() {
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    private static final class InstanceHolder {
        private static final Logger instance = new Logger();
    }

    /**
     * Trả về instance duy nhất của Logger (theo Singleton pattern).
     *
     * @return Instance duy nhất của Logger.
     */
    public static Logger getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * Ghi log một thông báo với mức độ log và thông báo cụ thể.
     * Các cột sẽ được căn chỉnh để dễ đọc hơn.
     *
     * @param level   Mức độ của log (INFO, DEBUG, hoặc ERROR).
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo log cần ghi.
     */
    private void log(String level, Class<?> clazz, String message) {
        String timestamp = LocalTime.now().format(timeFormatter);
        String className = clazz.getSimpleName();
        String logMessage = String.format("[%s] [%s] %-20s: %s", timestamp, level, className, message);
        System.out.println(logMessage);
    }

    /**
     * Ghi log một thông báo ở mức độ INFO.
     *
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo cần ghi ở mức độ INFO.
     */
    public void info(Class<?> clazz, String message) {
        log("INFO", clazz, message);
    }

    /**
     * Ghi log một thông báo ở mức độ DEBUG.
     *
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo cần ghi ở mức độ DEBUG.
     */
    public void debug(Class<?> clazz, String message) {
        log("DEBUG", clazz, message);
    }

    /**
     * Ghi log một thông báo ở mức độ ERROR.
     *
     * @param clazz   Lớp gọi đến phương thức log.
     * @param message Thông báo cần ghi ở mức độ ERROR.
     */
    public void error(Class<?> clazz, String message) {
        log("ERROR", clazz, message);
    }
}