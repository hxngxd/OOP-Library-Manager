package com.hxngxd.library_manager;

import com.hxngxd.database.DBManager;
import com.hxngxd.entities.User;
import com.hxngxd.service.UserService;

public class Main {

    public static void main(String[] args) {
        if (DBManager.connect()) {
            System.out.println("OK ROI");
        }
        User user = UserService.getUserByUsername("23020078");
        if (user != null) {
            System.out.println(user.getEmail());
        }
        if (DBManager.disconnect()) {
            System.out.println("OK ROI");
        }
    }
}