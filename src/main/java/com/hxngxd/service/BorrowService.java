package com.hxngxd.service;

import com.hxngxd.actions.Borrowing;

import java.util.List;

/**
 * Lớp BorrowService quản lý các thao tác mượn sách.
 */
public final class BorrowService {

    /**
     * Gửi yêu cầu mượn sách.
     *
     * @param borrowing Thông tin mượn sách.
     * @return true nếu yêu cầu thành công, false nếu thất bại.
     */
    public boolean request(Borrowing borrowing) {
        return true;
    }

    /**
     * Phê duyệt yêu cầu mượn sách.
     *
     * @param borrowing Thông tin mượn sách.
     * @return true nếu phê duyệt thành công, false nếu thất bại.
     */
    public boolean approve(Borrowing borrowing) {
        return true;
    }

    /**
     * Từ chối yêu cầu mượn sách.
     *
     * @param borrowing Thông tin mượn sách.
     * @return true nếu từ chối thành công, false nếu thất bại.
     */
    public boolean reject(Borrowing borrowing) {
        return true;
    }

    /**
     * Xử lý cho mượn sách khi đã được phê duyệt.
     *
     * @param borrowing Thông tin mượn sách.
     * @return true nếu cho mượn thành công, false nếu thất bại.
     */
    public boolean lendBook(Borrowing borrowing) {
        return true;
    }

    /**
     * Yêu cầu gia hạn thêm số ngày mượn sách.
     *
     * @param borrowing      Thông tin mượn sách.
     * @param additionalDays Số ngày muốn gia hạn.
     * @return true nếu gia hạn thành công, false nếu thất bại.
     */
    public boolean requestExtension(Borrowing borrowing, int additionalDays) {
        return true;
    }

    /**
     * Xử lý trả sách.
     *
     * @param borrowing Thông tin mượn sách.
     * @return true nếu trả sách thành công, false nếu thất bại.
     */
    public boolean returnBook(Borrowing borrowing) {
        return true;
    }

    /**
     * Huỷ yêu cầu mượn sách.
     *
     * @param borrowing Thông tin mượn sách.
     * @return true nếu hủy thành công, false nếu thất bại.
     */
    public boolean cancelRequest(Borrowing borrowing) {
        return true;
    }

    /**
     * Lấy danh sách yêu cầu mượn sách của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách yêu cầu mượn sách.
     */
    public List<Borrowing> getUsersRequests(int userId) {
        return null;
    }

    /**
     * Lấy danh sách yêu cầu mượn sách cho một quyển sách cụ thể.
     *
     * @param bookId ID của sách.
     * @return Danh sách yêu cầu mượn sách.
     */
    public List<Borrowing> getBookRequests(int bookId) {
        return null;
    }

    /**
     * Lấy danh sách yêu cầu mượn sách cần xử lý cho Moderator hoặc Admin.
     *
     * @param userId ID của Moderator hoặc Admin.
     * @return Danh sách các yêu cầu cần được xử lý.
     */
    public List<Borrowing> getRequests(int userId) {
        return null;
    }

}