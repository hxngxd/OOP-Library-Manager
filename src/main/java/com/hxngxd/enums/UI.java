package com.hxngxd.enums;

public enum UI {

    LOGIN,
    REGISTER,
    MAIN,
    BOOK_GALLERY,
    BOOK_CARD,
    BOOK_PREVIEW,
    BOOK_DETAIL,
    ACCOUNT,
    MANAGE_USER,
    MANAGE_BOOK,
    USER_REVIEW,
    POPUP;


    public String getPath() {
        return this.name() + ".fxml";
    }

}