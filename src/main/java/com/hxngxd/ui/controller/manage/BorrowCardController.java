package com.hxngxd.ui.controller.manage;

import com.hxngxd.entities.Book;
import com.hxngxd.ui.controller.book.BookDisplayController;
import com.hxngxd.utils.ImageHandler;

public class BorrowCardController extends BookDisplayController {

    @Override
    public void setBook(Book book) {
        super.setBook(book);

        setImageView(ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5));
        ImageHandler.roundCorner(this.imageView, 15);

        setName(book.getTitle());
        setInformation(book.toString());
    }

}
