package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.Book;
import com.hxngxd.ui.controller.ItemDisplayController;

public abstract class BookDisplayController extends ItemDisplayController {

    protected Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}