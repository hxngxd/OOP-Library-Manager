package com.hxngxd.ui;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @FXML
    private FlowPane bookDisplayContainer;

    public void displayBooks() {
        try {
            for (Book book : BookService.currentBooks) {
                VBox bookBox = FXMLLoader.load(
                        Objects.requireNonNull(
                                HomeController.class.getResource(
                                        UI.BOOK_DISPLAY.getPath())));
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
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
