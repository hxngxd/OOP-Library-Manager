package com.hxngxd.enums;

public enum BorrowStatus {

    PENDING,
    APPROVED,
    BORROWED,
    RETURNED_ON_TIME,
    OVERDUE,
    RETURNED_LATE,
    REJECTED;

    public String msg() {
        return switch (this) {
            case PENDING -> "Đang chờ phê duyệt";
            case APPROVED -> "Đã được phê duyệt";
            case BORROWED -> "Đang mượn";
            case RETURNED_ON_TIME -> "Đã trả đúng hạn";
            case OVERDUE -> "Quá hạn";
            case RETURNED_LATE -> "Đã trả trễ";
            case REJECTED -> "Bị từ chối";
        };
    }

    public String getGlyphName() {
        return switch (this) {
            case PENDING -> "HOURGLASS";
            case APPROVED -> "CHECK_CIRCLE";
            case BORROWED -> "BOOK";
            case RETURNED_ON_TIME -> "CIRCLE";
            case OVERDUE -> "EXCLAMATION_CIRCLE";
            case RETURNED_LATE -> "CLOCK_ALT";
            case REJECTED -> "BAN";
        };
    }

    public boolean canBorrowAgain() {
        return switch (this) {
            case RETURNED_ON_TIME, RETURNED_LATE -> true;
            case PENDING, APPROVED, BORROWED, OVERDUE, REJECTED -> false;
        };
    }

}