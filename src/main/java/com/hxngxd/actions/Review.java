package com.hxngxd.actions;

import com.hxngxd.entities.Book;

public final class Review extends Action {

    private Book book;

    private int rating;

    private String comment;

    public Review(int id) {
        super(id);
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getRating() {
        return rating;
    }

    public String getStringRating() {
        return "★".repeat(rating) + "☆".repeat(5 - rating) + " " + rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}