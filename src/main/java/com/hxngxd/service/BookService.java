package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.utils.ImageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BookService {
    private final Logger log = LogManager.getLogger(BookService.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    private Book currentBook = null;

    private BookService() {
    }

    private static class SingletonHolder {
        private static final BookService instance = new BookService();
    }

    public static BookService getInstance() {
        return BookService.SingletonHolder.instance;
    }

    public static void initialize() {
        BookService bookService = BookService.getInstance();
        try {
            bookService.getAllBooks();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public Book getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
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

    public void getAllBooks()
            throws DatabaseException {
        String query = "select book.*, bookauthor.authorId, bookgenre.genreId from book" +
                " join bookauthor on bookauthor.bookId = book.id" +
                " join bookgenre on bookgenre.bookId = book.id";
        db.select("getting books", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Book book = null;
                if (!Book.bookMap.containsKey(id)) {
                    book = new Book(
                            resultSet.getInt("id"),
                            resultSet.getTimestamp("dateAdded").toLocalDateTime(),
                            resultSet.getTimestamp("lastUpdated").toLocalDateTime()
                    );
                    book.setTitle(resultSet.getString("title"));
                    book.setYearOfPublication(resultSet.getShort("yearOfPublication"));
                    book.setShortDescription(resultSet.getString("shortDescription"));
                    book.setNumberOfPages(resultSet.getInt("numberOfPages"));
                    book.setAvailableCopies(resultSet.getInt("availableCopies"));
                    book.setTotalCopies(resultSet.getInt("totalCopies"));
                    byte[] coverImage = resultSet.getBytes("coverImage");
                    if (coverImage != null) {
                        book.setImage(ImageHandler.byteArrayToImage(coverImage));
                    }
                    Book.bookMap.put(id, book);
                } else {
                    book = Book.bookMap.get(id);
                }
                Author author = Author.authorMap.get(
                        resultSet.getInt("authorId")
                );
                Genre genre = Genre.genreMap.get(
                        resultSet.getInt("genreId")
                );
                book.addAuthor(author);
                book.addGenre(genre);
                author.addBook(book);
                genre.addBook(book);
            }
            return null;
        });
    }
}