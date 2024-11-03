package com.hxngxd.ui.controller;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BookCardController extends PreviewController {
    private Book book;
    @FXML
    private VBox cardContainer;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        setImage(
                ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
        );
        Rectangle clip = new Rectangle(this.image.getFitWidth(), this.image.getFitHeight());
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        this.image.setFitWidth(190);
        this.image.setFitHeight(285);
        this.image.setPreserveRatio(false);
        this.image.setClip(clip);
        
        setName(book.getTitle());
        setInformation(book.toString());
        cardContainer.setOnMouseClicked(mouseEvent -> previewBook());
    }

    private void previewBook() {
        try {
            SplitPane mainRoot = UIManager.loadOnce(UI.MAIN).getRoot();
            FXMLLoader loader = UIManager.loadOnce(UI.BOOK_PREVIEW);
            BookPreviewController bookPreviewController = loader.getController();
            AnchorPane bookPreviewRoot = loader.getRoot();
            if (!mainRoot.getItems().contains(bookPreviewRoot)) {
                mainRoot.getItems().add(bookPreviewRoot);
            }
            bookPreviewController.previewBook(this.book);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
