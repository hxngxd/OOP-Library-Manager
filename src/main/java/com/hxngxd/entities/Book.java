package com.hxngxd.entities;

import javafx.scene.image.Image;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private Image coverImage;

    public Book() {
    }

    public Book(int id, LocalDateTime dateAdded,
                LocalDateTime lastUpdated, double averageRating) {
        this.id = id;
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

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", shortDescription='" + shortDescription + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", dateAdded=" + dateAdded +
                ", lastUpdated=" + lastUpdated +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", averageRating=" + averageRating +
                '}';
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}