package com.hxngxd.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Book {
    private int id;
    private String title;
    private int yearOfPublication;
    private String shortDescription;
    // private Img coverImage;
    private int numberOfPages;
    private LocalDateTime dateAdded;
    private LocalDateTime lastUpdated;
    private int availableCopies;
    private int totalCopies;
    private double averageRating;

    public double calculateAverageRating(){
        return 0;
    }
}
