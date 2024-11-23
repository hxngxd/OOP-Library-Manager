package com.hxngxd.entities;

import com.hxngxd.actions.Review;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.BookService;
import com.hxngxd.utils.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public final class Book extends EntityWithPhoto {

    private static final Logger log = LogManager.getLogger(Book.class);

    private String title;
    private short yearOfPublication;
    private String shortDescription;
    private int numberOfPages;
    private int availableCopies;
    private int totalCopies;
    private double averageRating;
    private int numberOfReviews;

    private final Set<Author> authors = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
    private final Set<Review> reviews = new HashSet<>();

    public static final Set<Book> bookSet = new HashSet<>();
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

    public void addReviews(Review review) {
        reviews.add(review);
    }

    public Set<Review> getReviews() {
        return reviews;
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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Set<Genre> getGenres() {
        return genres;
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