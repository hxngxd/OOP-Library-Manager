package com.hxngxd.entities;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int user;
    private int book;
    private int rating;
    private String comment;
    private LocalDateTime reviewTime;

    public boolean editReview(int newRating, int newComment){
        return false;
    }
}
