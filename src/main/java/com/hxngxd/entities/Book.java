package com.hxngxd.entities;

import com.hxngxd.actions.Review;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.Formatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class Book extends EntityWithPhoto {

    private String title;

    private short yearOfPublication;

    private String shortDescription;

    private int numberOfPages;

    private int availableCopies;

    private int totalCopies;

    private double averageRating;

    private int numberOfReviews;

    private final List<Author> authors = new ArrayList<>();

    private final List<Genre> genres = new ArrayList<>();

    private final List<Review> reviews = new ArrayList<>();

    public static final List<Book> bookList = new ArrayList<>();

    public static final HashMap<Integer, Book> bookMap = new HashMap<>();

    public Book() {
    }

    public Book(int id) {
        super(id);
    }

    public Book(int id, LocalDateTime dateAdded) {
        super(id);
        this.dateAdded = dateAdded;
        setReview();
    }

    public Book(int id, String title, short yearOfPublication, String shortDescription,
                int numberOfPages, int availableCopies, int totalCopies) {
        this.id = id;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.shortDescription = shortDescription;
        this.numberOfPages = numberOfPages;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.averageRating = 0.0;
        setReview();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(short yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setReview() {
        this.averageRating = this.numberOfReviews = 0;
        DatabaseManager db = DatabaseManager.getInstance();
        String query = String.format(
                "select bookid, round(sum(rating) * 1.0 / count(rating), 2) as averagerating, " +
                        "count(rating) as numberofreviews " +
                        "from review where bookid = %d group by bookid;", this.id);
        try {
            db.select("calculating rating", query, resultSet -> {
                if (resultSet.next()) {
                    this.averageRating = resultSet.getDouble("averageRating");
                    this.numberOfReviews = resultSet.getInt("numberOfReviews");
                }
                return null;
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public String getReview() {
        if (numberOfReviews == 0) {
            return "Chưa được đánh giá";
        }
        int fullStars = (int) averageRating;
        String ratingStars = "★".repeat(fullStars) + "☆".repeat(5 - fullStars);
        return String.format("%.1f %s (%d)", averageRating, ratingStars, numberOfReviews);
    }

    public String getDetailReview() {
        if (numberOfReviews == 0) {
            return "Chưa được đánh giá";
        }
        int fullStars = (int) averageRating;
        String ratingStars = "★".repeat(fullStars) + "☆".repeat(5 - fullStars);
        return String.format("%s %.1f • %d lượt đánh giá", ratingStars, averageRating, numberOfReviews);
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void addAuthor(Author author) {
        if (!this.authors.contains(author)) {
            this.authors.add(author);
        }
    }

    public void addGenre(Genre genre) {
        if (!this.genres.contains(genre)) {
            this.genres.add(genre);
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Book)) {
            return false;
        }
        return this.id == ((Book) other).getId();
    }

    @Override
    public String toString() {
        return this.yearOfPublication + "\n" +
                authorsToString() + "\n" +
                genresToString() + "\n" +
                (this.averageRating == 0.0 ? "Chưa được đánh giá" : getReview());
    }

    public String toStringDetail() {
        return createInfo(
                "Mã sách: " + this.id,
                "Năm phát hành: " + this.yearOfPublication,
                "Số trang: " + this.numberOfPages,
                "Tác giả: " + authorsToString(),
                "Thể loại: " + genresToString(),
                "Mô tả ngắn: " + this.shortDescription,
                "Đánh giá: " + (this.averageRating == 0.0 ? "Chưa được đánh giá" : getReview()),
                "Thêm vào lúc: " + Formatter.formatDateTime(this.dateAdded),
                "Cập nhật cuối cùng: " + Formatter.formatDateTime(this.lastUpdated),
                "Số bản sao có sẵn: " + this.availableCopies + "/" + this.totalCopies
        );
    }

    public String toStringHalfDetail() {
        return createInfo(
                "Mã sách: " + this.id,
                "Số trang: " + this.numberOfPages,
                "Thêm vào lúc: " + Formatter.formatDateTime(this.dateAdded),
                "Cập nhật cuối cùng: " + Formatter.formatDateTime(this.lastUpdated),
                "Số bản sao có sẵn: " + this.availableCopies + "/" + this.totalCopies
        );
    }

    private String createInfo(String... lines) {
        return "• " + String.join("\n• ", lines);
    }

    public void genresToString(StringBuilder info) {
        info.append(genresToString()).append("\n");
    }

    public void authorsToString(StringBuilder info) {
        info.append(authorsToString()).append("\n");
    }

    public String genresToString() {
        return this.genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
    }

    public String authorsToString() {
        return this.authors.stream().map(Author::getFullNameFirstThenLast).collect(Collectors.joining(", "));
    }

    public void loadReviews() {
        reviews.clear();
        UserService.getInstance().getAllUsers();
        String query = "select * from review where bookId = ?";
        DatabaseManager.getInstance().select("load reviews", query, resultSet -> {
            while (resultSet.next()) {
                Review review = new Review(resultSet.getInt("id"));
                review.setUser(UserService.userMap.get(resultSet.getInt("userId")));
                review.setBook(BookService.bookMap.get(resultSet.getInt("bookId")));
                review.setRating(resultSet.getInt("rating"));
                review.setComment(resultSet.getString("comment"));
                review.setTimestamp(resultSet.getTimestamp("reviewTime").toLocalDateTime());
                reviews.add(review);
            }
            return null;
        }, id);
        reviews.sort(Comparator.comparing(Review::getTimestamp).reversed());
        setReview();
    }

    public List<Review> getReviews() {
        return reviews;
    }

}