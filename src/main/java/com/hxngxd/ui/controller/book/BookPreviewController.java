package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.MainController;
import com.hxngxd.ui.controller.BookGalleryController;
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

    protected Book book;

    @FXML
    private FontAwesomeIconView saveBookIcon;

    @FXML
    private Button saveBookButton;

    public void previewBook(Book book) {
        prv(book);
        setName(book.getTitle());
        setInformation(book.toStringDetail());
        setPreviewing(true);
    }

    protected void prv(Book book) {
        this.book = book;
        setImage(ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5));
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
                BookGalleryController.getInstance().showBookCardsBySearch();
                PopupManager.info("Đã lưu sách");
            } catch (DatabaseException e) {
//                e.printStackTrace();
                log.error(LogMsg.General.FAIL.getMSG("save book"), e.getMessage());
                PopupManager.info("Lỗi khi lưu sách");
            }
        } else {
            try {
                currentUser.unsaveBook(book);
                setSaveButtonState();
                BookGalleryController.getInstance().showBookCardsBySearch();
                PopupManager.info("Đã bỏ lưu sách");
            } catch (DatabaseException e) {
//                e.printStackTrace();
                log.error(LogMsg.General.FAIL.getMSG("unsave book"), e.getMessage());
                PopupManager.info("Lỗi khi bỏ lưu sách");
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

    @FXML
    public void showDetail() {
        UI ui = UI.BOOK_DETAIL;
        MainController mainController = MainController.getInstance();
        if (mainController.getCurrentTab() == ui) {
            return;
        }
        mainController.setCurrentTab(ui);
        mainController.navigate(UIManager.getRootOnce(ui));
        BookDetailController.getInstance().onActive(this.book);
    }

    public static BookPreviewController getInstance() {
        return UIManager.getActivableController(UI.BOOK_PREVIEW);
    }

    public Book getBook() {
        return book;
    }
}
