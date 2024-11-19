package com.hxngxd.actions;

import com.hxngxd.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public final class Notification extends Action {

    private String content;

    private final NotificationType notificationType = null;

    private boolean isRead;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public boolean isRead() {
        return isRead;
    }

    public static boolean sendNotification(Notification notification) {
        return true;
    }

    public boolean markAsRead(int notificationId) {
        return true;
    }

    public boolean markAsUnread(int notificationId) {
        return true;
    }

    public static List<Notification> getAllNotificationOfUsers(int userId) {
        return null;
    }

    public static List<Notification> getReadNotificationsOfUsers(int userId) {
        return null;
    }

    public static List<Notification> getUnreadNotificationsOfUsers(int userId) {
        return null;
    }

}