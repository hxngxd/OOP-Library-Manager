package com.hxngxd.enums;

import java.util.Set;

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
            Permission.MANAGE_SYSTEM,
            Permission.CHANGE_OTHER_PASSWORD_EMAIL
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

}