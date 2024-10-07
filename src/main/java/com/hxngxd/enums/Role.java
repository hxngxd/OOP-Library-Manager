package com.hxngxd.enums;

import java.util.Set;

/**
 * Enum Role đại diện cho các vai trò trong hệ thống, kèm theo các quyền hạn.
 */
public enum Role {
    USER(Set.of(
            Permission.DELETE_OWN_ACCOUNT
    )),
    MODERATOR(Set.of(
            Permission.DELETE_LOWER_REVIEWS,
            Permission.MANAGE_BOOKS,
            Permission.SEND_NOTIFICATIONS
    )),
    ADMIN(Set.of(
            Permission.DELETE_LOWER_REVIEWS,
            Permission.DELETE_LOWER_ACCOUNT,
            Permission.CHANGE_OTHERS_ROLE,
            Permission.CHANGE_OTHERS_ACCOUNT_STATUS,
            Permission.EDIT_OTHERS_PROFILE,
            Permission.MANAGE_BOOKS,
            Permission.SEND_NOTIFICATIONS,
            Permission.MANAGE_SYSTEM
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Kiểm tra xem vai trò có quyền hạn này hay không.
     *
     * @param permission Quyền hạn cần kiểm tra.
     * @return true nếu vai trò có quyền hạn này, ngược lại false.
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * Lấy danh sách các quyền hạn của vai trò.
     *
     * @return Tập hợp các quyền hạn.
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }
}