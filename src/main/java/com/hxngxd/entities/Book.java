package com.hxngxd.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "hh:mm:ss, dd-MM-yyyy");

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

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append(this.yearOfPublication).append("\n");
        authorsToString(info);
        genresToString(info);
        info.append(this.averageRating == 0.0
                ? "Chưa được đánh giá"
                : String.valueOf(this.averageRating));
        return info.toString();
    }

    public String toStringDetail() {
        String bullet = "• ";
        StringBuilder info = new StringBuilder();
        info.append(bullet).append("Năm phát hành: ").append(this.yearOfPublication).append("\n");
        info.append(bullet).append("Số trang: ").append(this.numberOfPages).append("\n");
        info.append(bullet).append("Tác giả: ");
        authorsToString(info);
        info.append(bullet).append("Thể loại: ");
        genresToString(info);
        info.append(bullet).append("Mô tả: ").append(this.shortDescription).append("\n");
        info.append(bullet).append("Đánh giá: ").append(
                this.averageRating == 0.0
                        ? "Chưa được đánh giá"
                        : String.valueOf(this.averageRating)).append("\n");
        info.append(bullet).append("Thêm vào lúc: ").append(
                this.dateAdded.format(formatter)).append("\n");
        info.append(bullet).append("Cập nhật cuối cùng: ").append(
                this.lastUpdated.format(formatter)).append("\n");
        info.append(bullet).append("Số bản sao có sẵn: ").append(
                this.availableCopies).append("/").append(this.totalCopies);
        return info.toString();
    }

    private void genresToString(StringBuilder info) {
        for (int i = 0; i < this.genres.size(); i++) {
            info.append(this.genres.get(i).getName());
            if (i < this.genres.size() - 1) {
                info.append(", ");
            }
        }
        info.append("\n");
    }

    private void authorsToString(StringBuilder info) {
        for (int i = 0; i < this.authors.size(); i++) {
            info.append(this.authors.get(i).getFullNameFirstThenLast());
            if (i < this.authors.size() - 1) {
                info.append(", ");
            }
        }
        info.append("\n");
    }
}