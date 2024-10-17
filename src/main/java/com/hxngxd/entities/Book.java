package com.hxngxd.entities;

import java.time.LocalDateTime;

public class Book {
    private int id;
    private String title;
    private short yearOfPublication;
    private String shortDescription;
    private int numberOfPages;
    private LocalDateTime dateAdded;
    private LocalDateTime lastUpdated;
    private int availableCopies;
    private int totalCopies;
    private double averageRating;

    public double calculateAverageRating() {
        return 0.0;
    }
}