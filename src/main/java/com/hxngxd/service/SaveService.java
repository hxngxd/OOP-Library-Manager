package com.hxngxd.service;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;

import java.util.List;

/**
 * Lớp SaveService quản lý việc lưu sách vào danh sách yêu thích của người dùng.
 */
public class SaveService {

    /**
     * Lưu sách vào danh sách yêu thích.
     *
     * @param bookId ID của sách.
     * @return true nếu lưu thành công, false nếu thất bại.
     */
    public boolean saveBook(int bookId) {
        return true;
    }

    /**
     * Xóa sách khỏi danh sách yêu thích.
     *
     * @param bookId ID của sách.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean removeSavedBook(int bookId) {
        return true;
    }

    /**
     * Lấy danh sách người dùng đã lưu sách nào đó vào danh sách yêu thích.
     *
     * @param bookId ID của sách.
     * @return Danh sách người dùng đã lưu sách.
     */
    public List<User> getSavingUsers(int bookId) {
        return null;
    }

    /**
     * Lấy danh sách sách mà người dùng đã lưu vào danh sách yêu thích.
     *
     * @param userId ID của người dùng.
     * @return Danh sách sách đã lưu.
     */
    public List<Book> getUsersSavedBooks(int userId) {
        return null;
    }
}