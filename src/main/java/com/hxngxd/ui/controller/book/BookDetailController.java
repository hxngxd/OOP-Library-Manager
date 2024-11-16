package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class BookDetailController extends BookPreviewController {

    @FXML
    private Label authorsLabel;

    @FXML
    private Label genresLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private VBox reviewsVbox;

    @FXML
    private Label reviewHeader;

    public void onActive(Book book) {
        super.prv(book);
        setName(book.getTitle() + " (" + book.getYearOfPublication() + ")");
        StringBuilder authors = new StringBuilder("Bởi • ");
        book.authorsToString(authors);
        StringBuilder genres = new StringBuilder("Thuộc thể loại • ");
        book.genresToString(genres);
        authorsLabel.setText(authors.toString());
        genresLabel.setText(genres.toString());
        ratingLabel.setText(book.getDetailReview());
        descriptionLabel.setText(book.getShortDescription());
        informationLabel.setText(book.toStringHalfDetail());
        displayReviews();
    }

    private void displayReviews() {
        reviewsVbox.getChildren().clear();
        book.loadReviews();
        System.out.println(book.getReviews().size());
        if (book.getNumberOfReviews() == 0) {
            return;
        }
        reviewsVbox.getChildren().add(reviewHeader);
        for (Review review : book.getReviews()) {
            FXMLLoader loader = UIManager.load(UI.USER_REVIEW);
            UserReviewController urc = loader.getController();
            urc.display(review);
            if (!reviewsVbox.getChildren().contains(loader.getRoot())) {
                reviewsVbox.getChildren().add(loader.getRoot());
            }
        }
    }

    public static BookDetailController getInstance() {
        return UIManager.getControllerOnce(UI.BOOK_DETAIL);
    }

}