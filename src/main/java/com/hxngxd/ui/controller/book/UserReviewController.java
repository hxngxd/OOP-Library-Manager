package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.entities.User;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
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

    public void display(Review review, boolean deletable, boolean editable) {
        ImageHandler.circleCrop(image, 50);
        image.setImage(ImageHandler.cropImageByRatio(review.getUser().getImage(), 1, 1));

        userLabel.setText(review.getUser().getFullNameLastThenFirst()
                + (review.getUser().getId() == User.getCurrent().getId() ? " (Bạn)" : ""));

        elapseLabel.setText(Formatter.timeElapsed(review.getTimestamp()));

        userRatingLabel.setText(review.getStringRating());

        userReviewLabel.setText(review.getComment());

        if (deletable) {
            List<Pair<String, Runnable>> buttons = new ArrayList<>();

            if (editable) {
                buttons.add(new Pair<>("CHANGE YOUR THOUGHT", () -> {
                    ((BookDetailController) UIManager.getController(UI.BOOK_DETAIL)).editReview(review);
                }));
            }

            buttons.add(new Pair<>("DELETE REVIEW", () -> {
                ((BookDetailController) UIManager.getController(UI.BOOK_DETAIL)).deleteReview(review);
            }));

            buttons.add(new Pair<>("CANCEL", PopupManager::closePopup));

            container.setOnMouseClicked(event -> {
                PopupManager.navigate("Edit review", buttons);
            });
        }
    }

}
