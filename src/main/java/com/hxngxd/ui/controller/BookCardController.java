package com.hxngxd.ui.controller;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class BookCardController extends PreviewController {
    private Book book;
    @FXML
    private VBox cardContainer;

    public void setBook(Book book) {
        this.book = book;
        setImage(
                ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
        );
        setName(book.getTitle());
        setInformation(book.toString());
        cardContainer.setOnMouseClicked(mouseEvent -> previewBook());
    }

    private void previewBook() {
        try {
            SplitPane mainRoot = UIManager.loadOnce(UI.MAIN).getRoot();
            FXMLLoader loader = UIManager.loadOnce(UI.BOOK_PREVIEW);
            BookPreviewController bookPreviewController = loader.getController();
            ScrollPane bookPreviewRoot = loader.getRoot();
            if (!mainRoot.getItems().contains(bookPreviewRoot)) {
                mainRoot.getItems().add(bookPreviewRoot);
            }
            bookPreviewController.previewBook(this.book);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
