package com.hxngxd.entities;

import java.time.LocalDate;
import java.util.List;

/**
 * Lớp Author kế thừa từ Person, đại diện cho tác giả của sách.
 */
public class Author extends Person {

    private String biography;
    private LocalDate dayOfDeath;

    public Author(int id, String firstName, String lastName, LocalDate dateOfBirth) {
        super(id, firstName, lastName, dateOfBirth);
    }

    /**
     * Thêm sách vào danh sách của tác giả.
     *
     * @param bookId ID của sách.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addBook(int bookId) {
        return true;
    }

    /**
     * Xóa sách khỏi danh sách của tác giả.
     *
     * @param bookId ID của sách.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean removeBook(int bookId) {
        return true;
    }

    /**
     * Lấy danh sách sách của tác giả.
     *
     * @return Danh sách sách của tác giả.
     */
    public List<Book> getBooks() {
        return null;
    }

}