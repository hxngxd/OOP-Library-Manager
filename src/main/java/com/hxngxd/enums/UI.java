package com.hxngxd.enums;

public enum UI {
    LOGIN,
    REGISTER,
    HOME,
    BOOK_DISPLAY;

    public String getPath() {
        return this.name() + ".fxml";
    }
}