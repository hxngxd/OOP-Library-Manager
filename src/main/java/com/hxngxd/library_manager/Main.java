package com.hxngxd.library_manager;

import com.hxngxd.database.DBManager;

public class Main {

    public static void main(String[] args) {
        if (DBManager.connect()) {
            System.out.println("OK ROI");
        }
        if (DBManager.disconnect()) {
            System.out.println("OK ROI");
        }
    }
}