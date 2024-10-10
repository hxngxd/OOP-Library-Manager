package com.hxngxd.service;

import com.hxngxd.entities.Notification;

import java.util.List;

/**
 * Lớp NotificationService quản lý việc gửi và quản lý thông báo cho người dùng.
 */
public class NotificationService {

    /**
     * Gửi thông báo đến người dùng.
     *
     * @param notification Thông báo cần gửi.
     * @return true nếu gửi thành công, false nếu thất bại.
     */
    public boolean sendNotification(Notification notification) {
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
    public List<Notification> getAllUsersNotification(int userId) {
        return null;
    }

    /**
     * Lấy danh sách thông báo đã đọc của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách thông báo đã đọc.
     */
    public List<Notification> getReadNotification(int userId) {
        return null;
    }

    /**
     * Lấy danh sách thông báo chưa đọc của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách thông báo chưa đọc.
     */
    public List<Notification> getUnreadNotifications(int userId) {
        return null;
    }

}