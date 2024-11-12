package com.hxngxd.enums;

public enum BorrowStatus {

    PENDING,         // Yêu cầu mượn đang chờ duyệt.
    APPROVED,        // Yêu cầu đã được phê duyệt.
    BORROWED,        // Sách đã được mượn.
    RETURNED_ON_TIME, // Sách trả đúng hạn.
    OVERDUE,         // Sách đang mượn quá hạn.
    RETURNED_LATE,   // Sách trả trễ hạn.
    REJECTED         // Yêu cầu bị từ chối.
    
}