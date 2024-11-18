package com.hxngxd.ui.controller.book;

import com.hxngxd.actions.Review;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class UserReviewController {

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

    public void display(Review review) {
        ImageHandler.circleCrop(image, 50);
        image.setImage(
                ImageHandler.cropImageByRatio(review.getUser().getImage(), 1, 1)
        );
        userLabel.setText(review.getUser().getFullNameLastThenFirst()
                + (review.getUser().getId() == UserService.getInstance().getCurrentUser().getId() ? " (Bạn)" : ""));
        elapseLabel.setText(Formatter.timeElapsed(review.getTimestamp()));
        userRatingLabel.setText(review.getStringRating());
        userReviewLabel.setText(review.getComment());
    }
}
