package com.hxngxd.library_manager;

import com.hxngxd.database.DBManager;

public class Main {

    public static void main(String[] args) {
        boolean connect = DBManager.connect();
        boolean disconnect = DBManager.disconnect();
    }
}