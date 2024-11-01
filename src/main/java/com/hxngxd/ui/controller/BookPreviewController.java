package com.hxngxd.ui.controller;

import com.hxngxd.entities.Book;
import com.hxngxd.utils.ImageHandler;

public class BookPreviewController extends PreviewController {
    private boolean isPreviewing = false;

    public void previewBook(Book book) {
        setImage(
                ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
        );
        setName(book.getTitle());
        setInformation(book.toStringDetail());
        setPreviewing(true);
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public void setPreviewing(boolean previewing) {
        isPreviewing = previewing;
    }
}
