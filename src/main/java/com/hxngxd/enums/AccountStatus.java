package com.hxngxd.enums;

/**
 * Enum AccountStatus đại diện cho trạng thái tài khoản.
 */
public enum AccountStatus {
    ACTIVE,      // Tài khoản đang hoạt động bình thường.
    INACTIVE,    // Tài khoản bị vô hiệu hóa tạm thời.
    SUSPENDED,   // Tài khoản bị đình chỉ do vi phạm (5 lần sẽ bị BANNED).
    BANNED       // Tài khoản bị cấm và không thể phục hồi.
}