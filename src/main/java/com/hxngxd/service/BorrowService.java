package com.hxngxd.service;

public final class BorrowService {

    private BorrowService() {
    }

    private static class SingletonHolder {
        private static final BorrowService instance = new BorrowService();
    }

    public static BorrowService getInstance() {
        return BorrowService.SingletonHolder.instance;
    }

}