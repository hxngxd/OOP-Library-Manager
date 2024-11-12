package com.hxngxd.enums;

public enum Permission {

    DELETE_LOWER_REVIEWS,          // Xoá đánh giá sách của chức thấp hơn
    DELETE_OWN_ACCOUNT,            // Xoá tài khoản của mình
    DELETE_LOWER_ACCOUNT,          // Xoá tài khoản chức thấp hơn
    CHANGE_OTHERS_ROLE,            // Thay đổi chức của các tài khoản khác trừ Admin
    CHANGE_OTHERS_ACCOUNT_STATUS,  // Thay đổi trạng thái của các tài khoản khác trừ Admin
    CHANGE_OTHER_PASSWORD_EMAIL,
    EDIT_OTHERS_PROFILE,           // Chỉnh sửa thông tin cá nhân của tài khoản khác
    MANAGE_BOOKS,                  // Quản lý sách (thêm, sửa, xoá, xử lý yêu cầu)
    SEND_NOTIFICATIONS,            // Gửi thông báo
    MANAGE_SYSTEM                  // Quản lý hệ thống (sao lưu, phục hồi, thống kê)
    
}