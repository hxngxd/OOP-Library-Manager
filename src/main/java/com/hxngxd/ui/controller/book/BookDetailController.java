package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.BorrowingRequestController;
import com.hxngxd.ui.controller.MainController;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

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

    private int currentStar = 0;

    @FXML
    private HBox starContainer;

    private final Image star = ImageHandler.loadImageFromResource("star.png");
    private final Image star_empty = ImageHandler.loadImageFromResource("star_empty.png");

    @FXML
    private Button borrrowButton;

    @Override
    public void onActive() {
        super.onActive();

        setName(book.getTitle() + " (" + book.getYearOfPublication() + ")");
        authorsLabel.setText("Bởi • " + book.authorsToString());
        genresLabel.setText("Thuộc thể loại • " + book.genresToString());
        descriptionLabel.setText(book.getShortDescription());
        informationLabel.setText(book.toStringHalfDetail());

        commentBox();
        starHover();
        displayReviews();
        ratingLabel.setText(book.getDetailedRating());

//        borrrowButton.setVisible(User.getCurrent().getRole() == Role.USER);
    }

    private void commentBox() {
        ImageHandler.circleCrop(userImage, 50);
        userImage.setImage(
                ImageHandler.cropImageByRatio(User.getCurrent().getImage(), 1, 1)
        );
        userLabel.setText(User.getCurrent().getFullNameLastThenFirst());

        userComment.setText("");
        userComment.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    userComment.appendText("\n");
                } else {
                    if (currentStar != 0 && !userComment.getText().isEmpty()) {
                        PopupManager.confirm("Confirm your review?", this::confirmReview);
                    }
                }
                event.consume();
            }
        });
    }

    private void confirmReview() {
        PopupManager.closePopup();
        try {
            DatabaseManager.getInstance().insert("review", false,
                    List.of("userId", "bookId", "rating", "comment"),
                    User.getCurrent().getId(), book.getId(), currentStar, userComment.getText());
            onActive();
            PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("add review"));
        } catch (DatabaseException e) {
            PopupManager.info(LogMsg.GENERAL_FAIL.msg("add review"));
        }
    }

    public void editReview(Review review) {
        PopupManager.confirmLargeInput("Edit your review", "Your comment...", review.getComment(), () -> {
            if (PopupManager.getInputPeek().getText().isEmpty()) {
                return;
            }
            try {
                DatabaseManager.getInstance().update("review",
                        "comment", PopupManager.getInputPeek().getText(), "id", review.getId());
                PopupManager.popInput();
                PopupManager.closePopup();
                PopupManager.closePopup();
                onActive();
                PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("edit review"));
            } catch (DatabaseException e) {
                PopupManager.info(LogMsg.GENERAL_FAIL.msg("edit review"));
            }
        });
    }

    public void deleteReview(Review review) {
        PopupManager.closePopup();
        try {
            DatabaseManager.getInstance().delete("review", "id", review.getId());
            PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("delete review"));
            onActive();
        } catch (DatabaseException e) {
            PopupManager.info(LogMsg.GENERAL_FAIL.msg("delete review"));
        }
    }

    private void starHover() {
        for (int j = 0; j < 5; j++) {
            setStarAt(star_empty, j);
        }
        for (int i = 0; i < 5; i++) {
            int i_ = i;
            ImageView img = getStarAt(i);
            img.setOnMouseEntered(event -> {
                for (int j = 0; j < 5; j++) {
                    setStarAt(j <= i_ ? star : star_empty, j);
                }
            });
            img.setOnMouseExited(event -> {
                for (int j = 0; j < 5; j++) {
                    setStarAt(j < currentStar ? star : star_empty, j);
                }
            });
            img.setOnMouseClicked(event -> {
                for (int j = 0; j < 5; j++) {
                    setStarAt(j <= i_ ? star : star_empty, j);
                }
                currentStar = i_ + 1;
            });
        }
    }

    private ImageView getStarAt(int i) {
        return (ImageView) starContainer.getChildren().get(i);
    }

    private void setStarAt(Image image, int i) {
        getStarAt(i).setImage(image);
    }

    private void displayReviews() {
        User user = User.getCurrent();

        reviewsVbox.getChildren().clear();
        reviewsVbox.getChildren().add(reviewHeader);
        BookService.getInstance().setReviews(book);

        boolean alreadyReviewed = false;
        for (Review review : book.getReviews()) {
            if (review.getUser().getId() == user.getId()) {
                alreadyReviewed = true;
                break;
            }
        }

        if (!alreadyReviewed) {
            reviewsVbox.getChildren().add(userReviewHbox);
        }

        if (book.getNumberOfReviews() == 0) {
            return;
        }

        for (Review review : book.getReviews()) {
            FXMLLoader loader = UIManager.load(UI.USER_REVIEW);
            UserReviewController urc = loader.getController();

            boolean ownReview = review.getUser().getId() == user.getId();
            if (user.getRole().hasPermission(Permission.MANAGE_REVIEWS)) {
                urc.display(review, true, ownReview);
            } else {
                urc.display(review, ownReview, true);
            }

            if (!reviewsVbox.getChildren().contains(loader.getRoot())) {
                reviewsVbox.getChildren().add(loader.getRoot());
            }
        }
    }

    @FXML
    private void requestBorrowing() {
        UI ui = UI.BORROWING_REQUEST;
        MainController mainController = UIManager.getController(UI.MAIN);
        if (mainController.getCurrentTab() == ui) {
            return;
        }
        mainController.setCurrentTab(ui);
        mainController.navigate(UIManager.getRootOnce(ui));
        ((BorrowingRequestController) UIManager.getController(UI.BORROWING_REQUEST)).setBook(book);
        UIManager.getActivableController(UI.BORROWING_REQUEST).onActive();
    }

}