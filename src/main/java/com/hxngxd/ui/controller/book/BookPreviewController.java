package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.Activable;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.MainController;
import com.hxngxd.ui.controller.BookGalleryController;
import com.hxngxd.utils.ImageHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BookPreviewController extends BookDisplayController implements Activable {

    private boolean isPreviewing = false;

    @FXML
    private FontAwesomeIconView saveBookIcon;

    @FXML
    private Button saveBookButton;

    @Override
    public void onActive() {
        setImageView(ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5));
        ImageHandler.roundCorner(imageView, 20);
        setSaveButtonState();
        setName(book.getTitle());
        setInformation(book.toStringDetail());
        isPreviewing = true;
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public void setPreviewing(boolean previewing) {
        isPreviewing = previewing;
    }

    @FXML
    private void toggleSaveBook(ActionEvent event) {
        User user = User.getCurrent();
        UserService userService = UserService.getInstance();
        BookGalleryController bookGalleryController = UIManager.getController(UI.BOOK_GALLERY);

        boolean saved = user.getSavedBooks().contains(book);
        try {
            userService.toggleSaveBook(book, !saved);
            setSaveButtonState();
            bookGalleryController.showBookCards();
            PopupManager.info(saved ? "Đã bỏ lưu sách" : "Đã lưu sách");
        } catch (DatabaseException e) {
            log.error(LogMsg.GENERAL_FAIL.msg(saved ? "unsave book" : "save book"), e);
            PopupManager.info(saved ? "Lỗi khi bỏ lưu sách" : "Lỗi khi lưu sách");
        }
    }

    private void setSaveButtonState() {
        boolean isSaved = User.getCurrent().getSavedBooks().contains(book);
        saveBookIcon.setGlyphName(isSaved ? "BOOKMARK" : "BOOKMARK_ALT");
        saveBookButton.setText(isSaved ? "BỎ LƯU SÁCH" : "LƯU SÁCH");
    }

    @FXML
    public void showDetail() {
        UI ui = UI.BOOK_DETAIL;
        MainController mainController = UIManager.getController(UI.MAIN);
        if (mainController.getCurrentTab() == ui) {
            return;
        }
        mainController.setCurrentTab(ui);
        mainController.navigate(UIManager.getRootOnce(ui));
        ((BookDetailController) UIManager.getController(UI.BOOK_DETAIL)).setBook(book);
        UIManager.getActivableController(UI.BOOK_DETAIL).onActive();
    }

}
