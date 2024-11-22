package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.Permission;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class BookDetailController extends BookPreviewController {

    private static final Logger log = LogManager.getLogger(BookDetailController.class);
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
        descriptionLabel.setText(book.getShortDescription());
        informationLabel.setText(book.toStringHalfDetail());

        ImageHandler.circleCrop(userImage, 50);
        userImage.setImage(
                ImageHandler.cropImageByRatio(userService.getCurrentUser().getImage(), 1, 1)
        );
        userLabel.setText(userService.getCurrentUser().getFullNameLastThenFirst());
        userComment.setText("");

        userComment.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    userComment.appendText("\n");
                } else {
                    if (currentStar != 0 && !userComment.getText().isEmpty()) {
                        PopupManager.confirm("Xác nhận đánh giá?", this::confirmReview);
                    }
                }
                event.consume();
            }
        });

        starHover();
        displayReviews();
        ratingLabel.setText(book.getDetailReview());
    }

    private void confirmReview() {
        PopupManager.closePopup();
        try {
            DatabaseManager.getInstance().insert("review", false,
                    List.of("userId", "bookId", "rating", "comment"),
                    userService.getCurrentUser().getId(), book.getId(), currentStar, userComment.getText());
            PopupManager.info("Đã đánh giá sách!");
            onActive(this.book);
            log.info(LogMsg.General.SUCCESS.getMSG("review"));
        } catch (DatabaseException e) {
            log.info(LogMsg.General.FAIL.getMSG("review"));
            PopupManager.info("Đánh giá thất bại!");
        }
    }

    public void editReviewComment(Review review) {
        PopupManager.confirmLargeInput("Sửa bình luận", "Bình luận của bạn...", review.getComment(), () -> {
            if (PopupManager.getInputPeek().getText().isEmpty()) {
                return;
            }
            try {
                DatabaseManager.getInstance().update(
                        "review", "comment", PopupManager.getInputPeek().getText(), "id", review.getId());
                PopupManager.popInput();
                PopupManager.closePopup();
                PopupManager.closePopup();
                onActive(this.book);
                log.info(LogMsg.General.SUCCESS.getMSG("edit comment"));
                PopupManager.info("Chỉnh sửa bình luận thành công!");
            } catch (DatabaseException e) {
                log.info(LogMsg.General.FAIL.getMSG("edit comment"));
                PopupManager.info("Chỉnh sửa bình luận thất bại!");
            }
        });
    }

    public void deleteReview(Review review) {
        PopupManager.closePopup();
        try {
            DatabaseManager.getInstance().delete("review", "id", review.getId());
            PopupManager.info("Đã xoá đánh giá!");
            onActive(this.book);
            log.info(LogMsg.General.SUCCESS.getMSG("delete review"));
        } catch (DatabaseException e) {
            log.info(LogMsg.General.FAIL.getMSG("delete review"));
            PopupManager.info("Xoá đánh giá thất bại!");
        }
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
        book.loadReviews();
        boolean alreadyReviewed = false;
        User user = userService.getCurrentUser();
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
            if (user.getRole().hasPermission(Permission.MANAGE_REVIEWS)) {
                urc.display(review, true, review.getUser().getId() == user.getId());
            } else {
                urc.display(review, review.getUser().getId() == user.getId(), true);
            }
            if (!reviewsVbox.getChildren().contains(loader.getRoot())) {
                reviewsVbox.getChildren().add(loader.getRoot());
            }
        }
    }

    public static BookDetailController getInstance() {
        return UIManager.getActivableController(UI.BOOK_DETAIL);
    }

}