package com.hxngxd.service;

import com.hxngxd.entities.Review;

import java.util.List;

/**
 * Lớp ReviewService quản lý các thao tác liên quan đến đánh giá sách.
 */
public class ReviewService {

    /**
     * Thêm đánh giá mới.
     *
     * @param review Đánh giá cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addReview(Review review) {
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
    public List<Review> getReviewsForBook(int bookId) {
        return null;
    }

    /**
     * Lấy danh sách đánh giá của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách các đánh giá của người dùng.
     */
    public List<Review> getUsersReviews(int userId) {
        return null;
    }

}