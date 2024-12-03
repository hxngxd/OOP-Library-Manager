package com.hxngxd.entities;

import java.time.LocalDate;
import java.util.*;

public final class Author extends Person {

    private String biography;
    private LocalDate dayOfDeath;

    private final Set<Book> books = new HashSet<>();

    public static final Set<Author> authorSet = new HashSet<>();
    public static final HashMap<Integer, Author> authorMap = new HashMap<>();

    public Author() {
    }

    public Author(int id) {
        this.id = id;
    }

    public Author(int id, String firstName, String lastName, LocalDate dateOfBirth,
                  String biography, LocalDate dayOfDeath) {
        super(id, firstName, lastName, dateOfBirth);
        this.biography = biography;
        this.dayOfDeath = dayOfDeath;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public LocalDate getDayOfDeath() {
        return dayOfDeath;
    }

    public void setDayOfDeath(LocalDate dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public void addBook(Book book) {
        books.add(book);
    }

}