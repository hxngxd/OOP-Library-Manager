package com.hxngxd.entities;

import java.util.List;

public class Genre {
    private int id;
    private String name;
    private String description;
    private List<Book> books;

    public boolean addBook(int bookId) {
        return true;
    }

    public boolean removeBook(int bookId) {
        return true;
    }

    public List<Book> getBooks() {
        return books;
    }
}
