package com.hxngxd.ui.controller.manage;

import com.hxngxd.actions.Borrowing;
import com.hxngxd.entities.Book;
import com.hxngxd.ui.controller.book.BookDisplayController;
import com.hxngxd.utils.ImageHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BorrowCardController extends BookDisplayController {

    @FXML
    private Label statusLabel;

    @FXML
    private FontAwesomeIconView statusIcon;

    @Override
    public void setBook(Book book) {
        super.setBook(book);

        setImageView(ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5));
        ImageHandler.roundCorner(this.imageView, 15);

        setName(String.format("%s (%d)", book.getTitle(), book.getYearOfPublication()));
    }

    public void setBorrowing(Borrowing borrowing) {
        setInformation(String.format("Bởi %s\n%s", book.authorsToString(), borrowing.toString()));
        statusLabel.setText(borrowing.getStatus().msg());
        statusIcon.setGlyphName(borrowing.getStatus().getGlyphName());
    }

}