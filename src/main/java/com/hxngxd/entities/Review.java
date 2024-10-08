package com.hxngxd.entities;

import java.time.LocalDateTime;

/**
 * Lớp Review đại diện cho đánh giá của người dùng về sách.
 */
public class Review {

    private int id;
    private int user;
    private int book;
    private int rating;
    private String comment;
    private LocalDateTime reviewTime;

    /**
     * Chỉnh sửa đánh giá.
     *
     * @param newRating  Điểm số mới.
     * @param newComment Bình luận mới.
     * @return true nếu chỉnh sửa thành công, false nếu thất bại.
     */
    public boolean editReview(int newRating, String newComment) {
        return true;
    }

}