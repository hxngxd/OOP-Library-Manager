package com.hxngxd.enums;

public enum UI {
    LOGIN,
    REGISTER,
    MAIN,
    BOOK_GALLERY,
    BOOK_CARD,
    BOOK_PREVIEW,
    ACCOUNT;

    public String getPath() {
        return this.name() + ".fxml";
    }
}