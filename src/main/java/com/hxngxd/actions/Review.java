package com.hxngxd.actions;

import java.time.LocalDateTime;
import java.util.List;

public class Review extends Action {
    private int bookId;
    private double rating;
    private String comment;

    public Review(int id, int userId, LocalDateTime timestamp, int bookId,
                  double rating, String comment) {
        super(id, userId, timestamp);
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public double getRating() {
        return rating;
    }


    public String getComment() {
        return comment;
    }

    public static boolean addReview(Review review) {
        return true;
    }

    public boolean editReview(int newRating, String newComment) {
        return true;
    }

    public boolean deleteReview(Review review) {
        return true;
    }

    public static List<Review> getReviewsForBook(int bookId) {
        return null;
    }

    public static List<Review> getUsersReviews(int userId) {
        return null;
    }
}