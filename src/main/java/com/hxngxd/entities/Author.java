package com.hxngxd.entities;

import java.time.LocalDate;
import java.util.List;

public class Author extends Person {
    private String biography;
    private LocalDate dayOfDeath;
    private List<Book> books;

    public Author() {
    }

    public Author(int id, String firstName, String lastName, LocalDate dateOfBirth,
                  String biography, LocalDate dayOfDeath, List<Book> books) {
        super(id, firstName, lastName, dateOfBirth);
        this.biography = biography;
        this.dayOfDeath = dayOfDeath;
        this.books = books;
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

    public List<Book> getBooks() {
        return books;
    }

    public boolean addBook(int bookId) {
        return true;
    }

    public boolean removeBook(int bookId) {
        return true;
    }
}