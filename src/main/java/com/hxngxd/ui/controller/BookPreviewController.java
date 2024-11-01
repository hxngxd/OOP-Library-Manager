package com.hxngxd.ui.controller;

import com.hxngxd.entities.Book;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class BookPreviewController extends PreviewController {
    @FXML
    private ScrollPane bookPreviewContainer;

    public void previewBook(Book book) {
        setImage(
                ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
        );
        setName(book.getTitle());
        setInformation(book.toStringDetail());
    }

    public ScrollPane getBookPreviewContainer() {
        return bookPreviewContainer;
    }
}
