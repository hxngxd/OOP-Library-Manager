package com.hxngxd.entities;

import com.hxngxd.entities.Book;

import java.util.List;

/**
 * Lớp Genre đại diện cho thể loại sách.
 */
public class Genre {

    private int id;
    private String name;
    private String description;

    /**
     * Thêm sách vào thể loại.
     *
     * @param bookId ID của sách.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addBook(int bookId) {
        return true;
    }

    /**
     * Xóa sách khỏi thể loại.
     *
     * @param bookId ID của sách.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean removeBook(int bookId) {
        return true;
    }

    /**
     * Lấy danh sách sách thuộc thể loại.
     *
     * @return Danh sách sách thuộc thể loại.
     */
    public List<Book> getBooks() {
        return null;
    }

}
