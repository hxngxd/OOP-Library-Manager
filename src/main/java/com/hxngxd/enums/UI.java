package com.hxngxd.enums;

public enum UI {

    LOGIN,
    REGISTER,
    MAIN,
    BOOK_GALLERY,
    BOOK_CARD,
    BOOK_PREVIEW,
    BOOK_DETAIL,
    BOOK_EDIT,
    ACCOUNT,
    MANAGE_USER,
    MANAGE_BOOK,
    USER_REVIEW,
    POPUP,
    BORROWING_REQUEST,
    MANAGE_BORROWING_0,
    MANAGE_BORROWING_1,
    BORROW_CARD;

    public String getPath() {
        return this.name() + ".fxml";
    }

}