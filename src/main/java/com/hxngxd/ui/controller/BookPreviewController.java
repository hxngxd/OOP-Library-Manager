package com.hxngxd.ui.controller;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.ImageHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookPreviewController extends PreviewController {
    private static final Logger log = LogManager.getLogger(BookPreviewController.class);
    private boolean isPreviewing = false;
    private Book book;
    @FXML
    private FontAwesomeIconView saveBookIcon;
    @FXML
    private Button saveBookButton;

    public void previewBook(Book book) {
        this.book = book;
        setImage(
                ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
        );
        setName(book.getTitle());
        setInformation(book.toStringDetail());
        setPreviewing(true);
        setSaveButtonState();
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public void setPreviewing(boolean previewing) {
        isPreviewing = previewing;
    }

    @FXML
    private void savedBook(ActionEvent event) {
        User currentUser = UserService.getInstance().getCurrentUser();
        if (!currentUser.getSavedBooks().contains(book)) {
            try {
                currentUser.saveBook(book);
                setSaveButtonState();
            } catch (DatabaseException e) {
                e.printStackTrace();
                log.error(LogMessages.General.FAIL.getMessage("save book"), e.getMessage());
            }
        } else {
            try {
                currentUser.unsaveBook(book);
                setSaveButtonState();
            } catch (DatabaseException e) {
                e.printStackTrace();
                log.error(LogMessages.General.FAIL.getMessage("unsave book"), e.getMessage());
            }
        }
    }

    private void setSaveButtonState() {
        User currentUser = UserService.getInstance().getCurrentUser();
        if (currentUser.getSavedBooks().contains(book)) {
            saveBookIcon.setGlyphName("BOOKMARK");
            saveBookButton.setText("BỎ LƯU SÁCH");
        } else {
            saveBookIcon.setGlyphName("BOOKMARK_ALT");
            saveBookButton.setText("LƯU SÁCH");
        }
    }
}
