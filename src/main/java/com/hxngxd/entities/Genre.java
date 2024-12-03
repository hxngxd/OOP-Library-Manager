package com.hxngxd.entities;

import java.util.*;

public final class Genre extends Entity {

    private String name;
    private String description;

    private final Set<Book> books = new HashSet<>();

    public static final HashMap<Integer, Genre> genreMap = new HashMap<>();

    public Genre() {
    }

    public Genre(int id) {
        super(id);
    }

    public Genre(int id, String name, String description) {
        super(id);
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.description = description;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Book> getBooks() {
        return books;
    }

}