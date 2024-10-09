package com.hxngxd.library_manager;

import com.hxngxd.database.DBManager;
import com.hxngxd.service.UserService;

public class Main {

    public static void main(String[] args) {
        boolean connect = DBManager.connect();
        UserService.loginByUsername("23020078", "Hung@07112005");
        boolean disconnect = DBManager.disconnect();
    }
}