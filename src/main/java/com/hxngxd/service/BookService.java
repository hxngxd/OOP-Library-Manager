package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.utils.ImageHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BookService {

    private final DatabaseManager db = DatabaseManager.getInstance();

    public static final List<Book> bookList = new ArrayList<>();

    public static final HashMap<Integer, Book> bookMap = new HashMap<>();

    private BookService() {
    }

    private static class SingletonHolder {
        private static final BookService instance = new BookService();
    }

    public static BookService getInstance() {
        return BookService.SingletonHolder.instance;
    }

    public static void initialize()
            throws DatabaseException {
        bookMap.clear();

        String query = "select book.*, bookauthor.authorId, bookgenre.genreId from book" +
                " join bookauthor on bookauthor.bookId = book.id" +
                " join bookgenre on bookgenre.bookId = book.id";
        DatabaseManager.getInstance().select("getting books", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Book book = null;

                if (!bookMap.containsKey(id)) {
                    book = new Book(
                            resultSet.getInt("id"),
                            resultSet.getTimestamp("dateAdded").toLocalDateTime()
                    );

                    Timestamp lastUpdated = resultSet.getTimestamp("lastUpdated");
                    if (lastUpdated != null) {
                        book.setLastUpdated(lastUpdated.toLocalDateTime());
                    }

                    book.setTitle(resultSet.getString("title"));

                    book.setYearOfPublication(resultSet.getShort("yearOfPublication"));

                    String description = resultSet.getString("shortDescription");
                    if (description != null) {
                        book.setShortDescription(description);
                    }

                    book.setNumberOfPages(resultSet.getInt("numberOfPages"));

                    book.setAvailableCopies(resultSet.getInt("availableCopies"));

                    book.setTotalCopies(resultSet.getInt("totalCopies"));

                    byte[] coverImage = resultSet.getBytes("coverImage");
                    if (coverImage != null) {
                        book.setImage(ImageHandler.byteArrayToImage(coverImage));
                    }

                    bookMap.put(id, book);
                } else {
                    book = bookMap.get(id);
                }

                Author author = Author.authorMap.get(resultSet.getInt("authorId"));
                Genre genre = Genre.genreMap.get(resultSet.getInt("genreId"));
                book.addAuthor(author);
                book.addGenre(genre);
                author.addBook(book);
                genre.addBook(book);
            }
            return null;
        });
    }

    public void getAllBooks() throws DatabaseException {
        bookList.clear();
        bookMap.clear();
        initialize();
        bookList.addAll(bookMap.values());
    }

    public void deleteBook(int bookId) throws DatabaseException {
        Book book = bookMap.get(bookId);
        if(book == null) {
            throw new DatabaseException("Book not found");
        }

        db.delete("book", "id", bookId);
        bookMap.remove(bookId);
        bookList.remove(book);
        System.out.println("Book with ID " + bookId + " has been successfully deleted.");

    }

}