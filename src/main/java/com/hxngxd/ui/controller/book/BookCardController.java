package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.ItemDisplayController;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public final class BookCardController extends BookDisplayController {

    @FXML
    private VBox cardContainer;

    @Override
    public void setBook(Book book) {
        super.setBook(book);

        setImageView(ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5));
        Rectangle clip = new Rectangle(this.imageView.getFitWidth(), this.imageView.getFitHeight());
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        this.imageView.setFitWidth(190);
        this.imageView.setFitHeight(285);
        this.imageView.setPreserveRatio(false);
        this.imageView.setClip(clip);

        setName(book.getTitle());
        setInformation(book.toString());

        cardContainer.setOnMouseClicked(mouseEvent -> {
            SplitPane main = UIManager.getRootOnce(UI.MAIN);
            AnchorPane bookPreview = UIManager.getRootOnce(UI.BOOK_PREVIEW);
            if (!main.getItems().contains(bookPreview)) {
                main.getItems().add(bookPreview);
            }
            ((BookPreviewController) UIManager.getController(UI.BOOK_PREVIEW)).setBook(book);
            UIManager.getActivableController(UI.BOOK_PREVIEW).onActive();
        });
    }

}