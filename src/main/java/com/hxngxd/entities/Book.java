package com.hxngxd.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book extends EntityWithPhoto {
    private String title;
    private short yearOfPublication;
    private String shortDescription;
    private int numberOfPages;
    private LocalDateTime dateAdded;
    private LocalDateTime lastUpdated;
    private int availableCopies;
    private int totalCopies;
    private double averageRating;
    private final List<Author> authors = new ArrayList<>();
    private final List<Genre> genres = new ArrayList<>();
    public static final HashMap<Integer, Book> bookMap = new HashMap<>();

    public Book() {
    }

    public Book(int id) {
        super(id);
    }

    public Book(int id, LocalDateTime dateAdded,
                LocalDateTime lastUpdated, double averageRating) {
        super(id);
        this.dateAdded = dateAdded;
        this.lastUpdated = lastUpdated;
        this.averageRating = averageRating;
    }

    public Book(int id, String title, short yearOfPublication, String shortDescription,
                int numberOfPages, int availableCopies, int totalCopies) {
        this.id = id;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.shortDescription = shortDescription;
        this.numberOfPages = numberOfPages;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.averageRating = 0.0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(short yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void calculateAverageRating() {
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void addAuthor(Author author) {
        if (!this.authors.contains(author)) {
            this.authors.add(author);
        }
    }

    public void addGenre(Genre genre) {
        if (!this.genres.contains(genre)) {
            this.genres.add(genre);
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Book)) {
            return false;
        }
        return this.id == ((Book) other).getId();
    }
}