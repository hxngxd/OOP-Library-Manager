package com.hxngxd.library_manager;

import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world");
        Role currentRole = Role.ADMIN;
        System.out.println(currentRole.hasPermission(Permission.CHANGE_OTHERS_ROLE));
    }
}