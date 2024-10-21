package com.hxngxd.ui;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.utils.ImageHandler;
import com.hxngxd.utils.LogMsg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Objects;

public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @FXML
    private FlowPane bookDisplayContainer;

    @FXML
    private ImageView profilePicture;

    public void displayBooks() {
        Collections.shuffle(BookService.currentBooks);
        for (Book book : BookService.currentBooks) {
            VBox bookBox = (VBox) UIManager.load(UI.BOOK_DISPLAY);
            assert bookBox != null;
            bookBox.setId(String.valueOf(book.getId()));
            ((Label) bookBox.lookup("#bookDisplayTitle")).setText(book.getTitle());
            ((Label) bookBox.lookup("#bookDisplayYear")).setText(
                    String.valueOf(book.getYearOfPublication())
            );
            ((Label) bookBox.lookup("#bookDisplayReview")).setText(
                    book.getAverageRating() == 0.0 ?
                            "Chưa được đánh giá" : String.valueOf(book.getAverageRating())
            );
            ((ImageView) bookBox.lookup("#bookDisplayCoverImage")).setImage(
                    ImageHandler.cropImageByRatio(book.getCoverImage(), 1, 1)
            );
            bookDisplayContainer.getChildren().add(bookBox);
        }
    }

    @FXML
    private void browseImage(ActionEvent event) {
        try {
            File file = ImageHandler.chooseImage("");
            if (file == null) {
                throw new Exception();
            }
            Image image = ImageHandler.cropImageByRatio(
                    Objects.requireNonNull(
                            ImageHandler.loadImageFromPath(file.getAbsolutePath())),
                    1, 1
            );
            profilePicture.setImage(image);
        } catch (Exception e) {
            logger.info(LogMsg.fail("loadOnce image"));
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}
