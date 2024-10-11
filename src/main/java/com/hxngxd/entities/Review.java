package com.hxngxd.entities;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * Thêm đánh giá mới.
     *
     * @param review Đánh giá cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public static boolean addReview(Review review) {
        return true;
    }

    /**
     * Xóa đánh giá.
     *
     * @param review Đánh giá cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteReview(Review review) {
        return true;
    }

    /**
     * Lấy danh sách đánh giá cho sách.
     *
     * @param bookId ID của sách.
     * @return Danh sách các đánh giá cho sách.
     */
    public static List<Review> getReviewsForBook(int bookId) {
        return null;
    }

    /**
     * Lấy danh sách đánh giá của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách các đánh giá của người dùng.
     */
    public static List<Review> getUsersReviews(int userId) {
        return null;
    }

}