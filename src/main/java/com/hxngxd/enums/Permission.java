package com.hxngxd.enums;

public enum Permission {
    CREATE_ACCOUNT,                // Người mới có thể tạo tài khoản
    SEARCH_VIEW_SAVE_BOOK,         // Tìm kiếm, xem, lưu sách
    REVIEW_BOOK,                   // Đánh giá sách
    EDIT_OWN_REVIEW,               // Xoá, chỉnh sửa đánh giá sách của mình
    DELETE_LOWER_REVIEWS,          // Xoá đánh giá sách của chức thấp hơn
    DELETE_OWN_ACCOUNT,            // Xoá tài khoản của mình
    DELETE_LOWER_ACCOUNT,          // Xoá tài khoản chức thấp hơn
    CHANGE_OWN_ROLE,               // Tự thay đổi chức của chính mình
    CHANGE_OTHERS_ROLE,            // Thay đổi chức của các tài khoản khác trừ Admin
    CHANGE_OWN_ACCOUNT_STATUS,     // Tự thay đổi trạng thái tài khoản (SUSPENDED, BANNED)
    CHANGE_OTHERS_ACCOUNT_STATUS,  // Thay đổi trạng thái của các tài khoản khác trừ Admin
    EDIT_OWN_PROFILE,              // Chỉnh sửa thông tin cá nhân của chính mình
    EDIT_OTHERS_PROFILE,           // Chỉnh sửa thông tin cá nhân của tài khoản khác
    MANAGE_BOOKS,                  // Quản lý sách (thêm, sửa, xoá, xử lý yêu cầu)
    SEND_NOTIFICATIONS,            // Gửi thông báo
    MANAGE_SYSTEM;                 // Quản lý hệ thống (sao lưu, phục hồi, thống kê)
}
