package com.hxngxd.enums;

public enum UI {

    LOGIN,
    REGISTER,
    MAIN,
    BOOK_GALLERY,
    BOOK_CARD,
    BOOK_PREVIEW,
    ACCOUNT,
    MANAGE_USER,
    MANAGE_POPUP,
    INFORMATION_POPUP,
    CONFIRMATION_POPUP,
    CONFIRMATION_INPUT_POPUP;

    public String getPath() {
        return this.name() + ".fxml";
    }

}