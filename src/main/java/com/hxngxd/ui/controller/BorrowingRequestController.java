package com.hxngxd.ui.controller;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.service.BorrowService;
import com.hxngxd.ui.Activable;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.book.BookPreviewController;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class BorrowingRequestController implements Activable {

    @FXML
    private TextField userIdField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private ImageView bookCover;

    @FXML
    private TextField bookIdField;

    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField authorsField;

    @FXML
    private TextField genresField;

    @FXML
    private DatePicker requestedDatePicker;

    @FXML
    private DatePicker estimatedReturnDatePicker;

    private Book book;

    @FXML
    private void initialize() {
        requestedDatePicker.setStyle("-fx-font-size: 16px;");
        estimatedReturnDatePicker.setStyle("-fx-font-size: 16px;");
    }

    @Override
    public void onActive() {
        User user = User.getCurrent();
        userIdField.setText(String.valueOf(user.getId()));
        lastNameField.setText(user.getLastName());
        firstNameField.setText(user.getFirstName());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());

        bookCover.setImage(ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5));
        ImageHandler.roundCorner(bookCover, 15);

        bookIdField.setText(String.valueOf(book.getId()));
        bookTitleField.setText(String.format("%s (%d)", book.getTitle(), book.getYearOfPublication()));
        authorsField.setText(book.authorsToString());
        genresField.setText(book.genresToString());

        requestedDatePicker.setValue(LocalDate.now());
        estimatedReturnDatePicker.setValue(LocalDate.now().plusDays(7));
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @FXML
    private void submitRequest() {
        PopupManager.confirm("Submit?", () -> {
            PopupManager.closePopup();
            try {
                BorrowService.getInstance().submitRequest(User.getCurrent(), book, estimatedReturnDatePicker.getValue());
                PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("submit request"));
                cancel();
            } catch (DatabaseException e) {
                PopupManager.info(LogMsg.GENERAL_FAIL.msg("submit request"));
            } catch (ValidationException e) {
                PopupManager.info(e.getMessage());
            }
        });
    }

    @FXML
    private void cancel() {
        ((BookPreviewController) UIManager.getController(UI.BOOK_PREVIEW)).showDetail();
    }

}
