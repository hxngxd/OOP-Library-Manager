package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class UserReviewController {

    @FXML
    private HBox container;

    @FXML
    private ImageView image;

    @FXML
    private Label userLabel;

    @FXML
    private Label elapseLabel;

    @FXML
    private Label userRatingLabel;

    @FXML
    private Label userReviewLabel;

    private Review review;

    public void display(Review review, boolean deletable, boolean editable) {
        ImageHandler.circleCrop(image, 50);
        this.review = review;
        image.setImage(
                ImageHandler.cropImageByRatio(review.getUser().getImage(), 1, 1)
        );
        userLabel.setText(review.getUser().getFullNameLastThenFirst()
                + (review.getUser().getId() == UserService.getInstance().getCurrentUser().getId() ? " (Bạn)" : ""));
        elapseLabel.setText(Formatter.timeElapsed(review.getTimestamp()));
        userRatingLabel.setText(review.getStringRating());
        userReviewLabel.setText(review.getComment());

        if (deletable) {
            List<Pair<String, Runnable>> btns = new ArrayList<>();
            if (editable) {
                btns.add(new Pair<>("SỬA BÌNH LUẬN", () -> {
                    BookDetailController.getInstance().editReviewComment(this.review);
                }));
            }
            btns.add(new Pair<>("XOÁ ĐÁNH GIÁ", () -> {
                BookDetailController.getInstance().deleteReview(this.review);
            }));
            btns.add(new Pair<>("HUỶ", PopupManager::closePopup));
            container.setOnMouseClicked(event -> {
                PopupManager.navigate("Chỉnh sửa đánh giá", btns);
            });
        }
    }

}
