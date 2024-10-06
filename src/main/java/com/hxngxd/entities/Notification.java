package com.hxngxd.entities;

import com.hxngxd.enums.NotificationType;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private int recipient;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime timestamp;
    private boolean is_read;
}
