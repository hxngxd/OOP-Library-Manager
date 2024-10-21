package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.utils.ImageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final Logger logger = LogManager.getLogger(UserService.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    public static final List<Book> currentBooks = new ArrayList<>();

    private BookService() {
    }

    private static class SingletonHolder {
        private static final BookService instance = new BookService();
    }

    public static BookService getInstance() {
        return BookService.SingletonHolder.instance;
    }

    public static void init() {
        BookService bookService = BookService.getInstance();
        bookService.getAllBooks();
    }

    public boolean updateAvailableCopies(int bookId, int difference) {
        return true;
    }

    public boolean updateTotalCopies(int bookId, int difference) {
        return true;
    }

    public boolean addAuthor(int bookId, int authorId) {
        return true;
    }

    public boolean removeAuthor(int bookId, int authorId) {
        return true;
    }

    public List<Author> getBooksAuthor(int bookId) {
        return null;
    }

    public boolean addGenre(int bookId, int genreId) {
        return true;
    }

    public boolean removeGenre(int bookId, int genreId) {
        return true;
    }

    public List<Genre> getBooksGenre(int bookId) {
        return null;
    }

    public List<Book> searchBooksByTitle(String title) {
        return null;
    }

    public List<Book> searchBooksByYear(short yearOfPublication) {
        return null;
    }

    public List<Book> searchBooksByDescription(String shortDescription) {
        return null;
    }

    public List<Book> searchBooksByRating(double averageRating) {
        return null;
    }

    public boolean addBook(Book book) {
        return false;
    }

    public boolean deleteBook(int id) {
        return true;
    }

    public Book getBookById(int id) {
        return null;
    }

    public boolean saveBook(int bookId) {
        return true;
    }

    public boolean removeSavedBook(int bookId) {
        return true;
    }

    public List<User> getSavingUsers(int bookId) {
        return null;
    }

    public List<Book> getUsersSavedBooks(int userId) {
        return null;
    }

    public void getAllBooks() {
        currentBooks.clear();
        String query = "select * from book";
        db.select("getting books", query, resultSet -> {
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("dateAdded").toLocalDateTime(),
                        resultSet.getTimestamp("lastUpdated").toLocalDateTime(),
                        resultSet.getDouble("averageRating")
                );
                book.setTitle(resultSet.getString("title"));
                book.setYearOfPublication(resultSet.getShort("yearOfPublication"));
                book.setShortDescription(resultSet.getString("shortDescription"));
                book.setNumberOfPages(resultSet.getInt("numberOfPages"));
                book.setAvailableCopies(resultSet.getInt("availableCopies"));
                book.setTotalCopies(resultSet.getInt("totalCopies"));
                book.setCoverImage(ImageHandler.byteToImage(
                        resultSet.getBytes("coverImage")
                ));
                currentBooks.add(book);
            }
            return null;
        });
    }
}