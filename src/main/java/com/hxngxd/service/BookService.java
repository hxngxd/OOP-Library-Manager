package com.hxngxd.service;

import com.hxngxd.actions.Review;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.utils.ImageHandler;

import java.sql.Timestamp;

public final class BookService extends Service<Book> {

    private BookService() {
    }

    private static class SingletonHolder {
        private static final BookService instance = new BookService();
    }

    public static BookService getInstance() {
        return BookService.SingletonHolder.instance;
    }

    @Override
    public void loadAll()
            throws DatabaseException {
        Book.bookSet.clear();
        Book.bookMap.clear();

        String query = "select book.*, bookauthor.authorId, bookgenre.genreId from book" +
                " join bookauthor on bookauthor.bookId = book.id" +
                " join bookgenre on bookgenre.bookId = book.id";
        DatabaseManager.getInstance().select("getting books", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Book book;

                if (!Book.bookMap.containsKey(id)) {
                    book = new Book(id);

                    book.setDateAdded(resultSet.getTimestamp("dateAdded").toLocalDateTime());

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

                    Book.bookMap.put(id, book);
                } else {
                    book = Book.bookMap.get(id);
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
        Book.bookSet.addAll(Book.bookMap.values());
    }

    public void setAllReviews() {
        for (Book book : Book.bookSet) {
            setReviews(book);
        }
    }

    public void setReviews(Book book) {
        book.getReviews().clear();
        book.setAverageRating(0);
        book.setNumberOfReviews(0);
        String query = "select * from review where bookId = ?";
        try {
            DatabaseManager.getInstance().select("load reviews", query, resultSet -> {
                double totalRating = 0;
                int reviewCount = 0;
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"));
                    review.setUser(User.userMap.get(resultSet.getInt("userId")));
                    review.setBook(book);
                    int rating = resultSet.getInt("rating");
                    review.setRating(rating);
                    review.setComment(resultSet.getString("comment"));
                    review.setTimestamp(resultSet.getTimestamp("reviewTime").toLocalDateTime());
                    book.addReviews(review);
                    totalRating += rating;
                    reviewCount++;
                }
                if (reviewCount > 0) {
                    book.setAverageRating(Math.round((totalRating / reviewCount) * 100.0) / 100.0);
                    book.setNumberOfReviews(reviewCount);
                }
                return null;
            }, book.getId());
        } catch (DatabaseException e) {
            log.error(LogMsg.GENERAL_FAIL.msg("set reviews"), e);
        }
    }

    public void deleteBook(int bookId)
            throws DatabaseException {
        if (!Book.bookMap.containsKey(bookId)) {
            return;
        }
        Book.bookSet.remove(Book.bookMap.get(bookId));
        Book.bookMap.remove(bookId);
        DatabaseManager.getInstance().delete("book", "id", bookId);
        log.info(LogMsg.GENERAL_SUCCESS.msg("delete book"));
    }

}