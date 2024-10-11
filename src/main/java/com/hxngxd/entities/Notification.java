package com.hxngxd.entities;

import com.hxngxd.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lớp Notification đại diện cho thông báo gửi đến người dùng.
 */
public class Notification {

    private int id;
    private int recipient;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime timestamp;
    private boolean isRead;

    /**
     * Gửi thông báo đến người dùng.
     *
     * @param notification Thông báo cần gửi.
     * @return true nếu gửi thành công, false nếu thất bại.
     */
    public static boolean sendNotification(Notification notification) {
        return true;
    }

    /**
     * Đánh dấu thông báo là đã đọc.
     *
     * @param notificationId ID của thông báo.
     * @return true nếu đánh dấu thành công, false nếu thất bại.
     */
    public boolean markAsRead(int notificationId) {
        return true;
    }

    /**
     * Đánh dấu thông báo là chưa đọc.
     *
     * @param notificationId ID của thông báo.
     * @return true nếu đánh dấu thành công, false nếu thất bại.
     */
    public boolean markAsUnread(int notificationId) {
        return true;
    }

    /**
     * Lấy danh sách tất cả thông báo của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách thông báo.
     */
    public static List<Notification> getAllUsersNotification(int userId) {
        return null;
    }

    /**
     * Lấy danh sách thông báo đã đọc của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách thông báo đã đọc.
     */
    public static List<Notification> getReadNotification(int userId) {
        return null;
    }

    /**
     * Lấy danh sách thông báo chưa đọc của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách thông báo chưa đọc.
     */
    public static List<Notification> getUnreadNotifications(int userId) {
        return null;
    }
}