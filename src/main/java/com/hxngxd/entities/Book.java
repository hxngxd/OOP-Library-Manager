package com.hxngxd.entities;

import java.time.LocalDateTime;

/**
 * Lớp Book đại diện cho một cuốn sách trong thư viện.
 */
public class Book {

    private int id;
    private String title;
    private short yearOfPublication;
    private String shortDescription;
    //    private Img coverImage;
    private int numberOfPages;
    private LocalDateTime dateAdded;
    private LocalDateTime lastUpdated;
    private int availableCopies;
    private int totalCopies;
    private double averageRating;

    /**
     * Tính điểm đánh giá trung bình.
     *
     * @return Điểm đánh giá trung bình.
     */
    public double calculateAverageRating() {
        return 0.0;
    }

}