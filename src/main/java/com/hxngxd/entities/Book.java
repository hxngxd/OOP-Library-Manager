package com.hxngxd.entities;

import com.hxngxd.actions.Review;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.Formatter;

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

    public Book(int id, String title, short yearOfPublication, String shortDescription,
                int numberOfPages, int availableCopies, int totalCopies) {
        this.id = id;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.shortDescription = shortDescription;
        this.numberOfPages = numberOfPages;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        setReviews();
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews() {
        reviews.clear();
        String query = "select * from review where bookId = ?";
        DatabaseManager.getInstance().select("load reviews", query, resultSet -> {
            double totalRating = 0;
            int reviewCount = 0;
            while (resultSet.next()) {
                Review review = new Review(resultSet.getInt("id"));
                review.setUser(UserService.userMap.get(resultSet.getInt("userId")));
                review.setBook(this);
                int rating = resultSet.getInt("rating");
                review.setRating(rating);
                review.setComment(resultSet.getString("comment"));
                review.setTimestamp(resultSet.getTimestamp("reviewTime").toLocalDateTime());
                reviews.add(review);
                totalRating += rating;
                reviewCount++;
            }
            if (reviewCount > 0) {
                this.averageRating = Math.round((totalRating / reviewCount) * 100.0) / 100.0;
                this.numberOfReviews = reviewCount;
            }
            return null;
        }, id);
        reviews.sort(Comparator.comparing(Review::getTimestamp).reversed());
    }

    private String getRatingStars() {
        int fullStars = (int) averageRating;
        return "★".repeat(fullStars) + "☆".repeat(5 - fullStars);
    }

    public String getRating() {
        return numberOfReviews == 0 ? "Chưa được đánh giá" :
                String.format("%.1f %s (%d)", averageRating, getRatingStars(), numberOfReviews);
    }

    public String getDetailedRating() {
        return numberOfReviews == 0 ? "Chưa được đánh giá" :
                String.format("%s %.1f • %d lượt đánh giá", getRatingStars(), averageRating, numberOfReviews);
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
        return String.format("%d\n%s\n%s\n%s",
                yearOfPublication,
                authorsToString(),
                genresToString(),
                getRating());
    }

    public String toStringDetail() {
        return createInfo(
                "Mã sách: " + id,
                "Năm phát hành: " + yearOfPublication,
                "Số trang: " + numberOfPages,
                "Tác giả: " + authorsToString(),
                "Thể loại: " + genresToString(),
                "Mô tả ngắn: " + shortDescription,
                "Đánh giá: " + getRating(),
                "Thêm vào lúc: " + Formatter.formatDateTime(dateAdded),
                "Cập nhật cuối cùng: " + Formatter.formatDateTime(lastUpdated),
                "Số bản sao có sẵn: " + availableCopies + "/" + totalCopies
        );
    }

    public String toStringHalfDetail() {
        return createInfo(
                "Mã sách: " + id,
                "Số trang: " + numberOfPages,
                "Thêm vào lúc: " + Formatter.formatDateTime(dateAdded),
                "Cập nhật cuối cùng: " + Formatter.formatDateTime(lastUpdated),
                "Số bản sao có sẵn: " + availableCopies + "/" + totalCopies
        );
    }

    private String createInfo(String... lines) {
        return "• " + String.join("\n• ", lines);
    }

    public String genresToString() {
        return genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
    }

    public String authorsToString() {
        return authors.stream().map(Author::getFullNameFirstThenLast).collect(Collectors.joining(", "));
    }

}