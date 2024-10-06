package com.hxngxd.entities;

import com.hxngxd.enums.NotificationType;

import java.time.LocalDateTime;

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

}