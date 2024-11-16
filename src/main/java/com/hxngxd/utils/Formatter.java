package com.hxngxd.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Formatter {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMATTER_DASH = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final DateTimeFormatter DATE_FORMATTER_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm:ss");

    private Formatter() {
    }

    public static String formatDateDash(LocalDate date) {
        return DATE_FORMATTER_DASH.format(date);
    }

    public static String formatDateSlash(LocalDate date) {
        return DATE_FORMATTER_SLASH.format(date);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    public static String formatTime(LocalDateTime dateTime) {
        return TIME_FORMATTER.format(dateTime);
    }

    public static String timeElapsed(LocalDateTime fromDateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(fromDateTime, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " giây trước";
        }

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + " phút trước";
        }

        long hours = duration.toHours();
        if (hours < 24) {
            return hours + " giờ trước";
        }

        long days = duration.toDays();
        if (days < 30) {
            return days + " ngày trước";
        }

        long months = days / 30;
        if (months < 12) {
            return months + " tháng trước";
        }

        long years = months / 12;
        return years + " năm trước";
    }

}
