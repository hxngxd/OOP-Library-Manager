package com.hxngxd.library_manager;

import com.hxngxd.database.DBManager;
import com.hxngxd.service.UserService;

public class Main {

    public static void main(String[] args) {
        boolean connect = DBManager.connect();
        UserService.loginByEmail("23020078@vnu.edu.vn", "Hung@07112005");
        boolean disconnect = DBManager.disconnect();
    }
}