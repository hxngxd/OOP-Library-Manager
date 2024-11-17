package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    @FXML
    private HBox userReviewHbox;

    @FXML
    private ImageView userImage;

    @FXML
    private Label userLabel;

    @FXML
    private TextArea userComment;

    private final UserService userService = UserService.getInstance();

    private int currentStar = 0;

    @FXML
    private HBox starContainer;

    private final Image star = ImageHandler.loadImageFromResource("star.png");

    private final Image star_empty = ImageHandler.loadImageFromResource("star_empty.png");

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

        ImageHandler.circleCrop(userImage, 50);
        userImage.setImage(
                ImageHandler.cropImageByRatio(userService.getCurrentUser().getImage(), 1, 1)
        );
        userLabel.setText(userService.getCurrentUser().getFullNameFirstThenLast());
        userComment.setText("");

        userComment.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    userComment.appendText("\n");
                } else {
                    System.out.println(userComment.getText());
                }
                event.consume();
            }
        });

        starHover();
        displayReviews();
    }

    private void starHover() {
        for (int j = 0; j < 5; j++) {
            ImageView prevImage = (ImageView) starContainer.getChildren().get(j);
            prevImage.setImage(star_empty);
        }

        for (int i = 0; i < 5; i++) {
            ImageView image = (ImageView) starContainer.getChildren().get(i);
            int finalI = i;
            image.setOnMouseEntered(event -> {
                for (int j = 0; j < 5; j++) {
                    ImageView prevImage = (ImageView) starContainer.getChildren().get(j);
                    prevImage.setImage(j <= finalI ? star : star_empty);
                }
            });
            image.setOnMouseExited(event -> {
                for (int j = 0; j < 5; j++) {
                    ImageView prevImage = (ImageView) starContainer.getChildren().get(j);
                    prevImage.setImage(j < currentStar ? star : star_empty);
                }
            });
            image.setOnMouseClicked(event -> {
                for (int j = 0; j < 5; j++) {
                    ImageView prevImage = (ImageView) starContainer.getChildren().get(j);
                    prevImage.setImage(j <= finalI ? star : star_empty);
                }
                currentStar = finalI + 1;
            });
        }
    }

    private void displayReviews() {
        reviewsVbox.getChildren().clear();
        reviewsVbox.getChildren().add(reviewHeader);
        reviewsVbox.getChildren().add(userReviewHbox);
        book.loadReviews();
        if (book.getNumberOfReviews() == 0) {
            return;
        }
        for (Review review : book.getReviews()) {
            FXMLLoader loader = UIManager.load(UI.USER_REVIEW);
            UserReviewController urc = loader.getController();
            urc.display(review);
            if (!reviewsVbox.getChildren().contains(loader.getRoot())) {
                reviewsVbox.getChildren().add(loader.getRoot());
            }
        }
    }

    @FXML
    private void addReview(ActionEvent event) {
    }

    public static BookDetailController getInstance() {
        return UIManager.getControllerOnce(UI.BOOK_DETAIL);
    }

}