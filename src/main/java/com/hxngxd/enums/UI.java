package com.hxngxd.enums;

public enum UI {
    LOGIN,
    REGISTER,
    HOME,
    BOOK_DISPLAY,
    BOOK_PREVIEW;

    public String getPath() {
        return this.name() + ".fxml";
    }
}