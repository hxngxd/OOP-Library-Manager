package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.ui.Activable;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.MainController;
import com.hxngxd.utils.ImageHandler;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public final class BookEditController implements Activable {

    private Book book;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField bookIdField;

    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField copiesField;

    @FXML
    private TextArea descriptionField;

    private File newImageFile;

    @Override
    public void onActive() {
        if (book != null) {
            bookIdField.setText(String.valueOf(book.getId()));
            bookTitleField.setText(book.getTitle());
            yearField.setText(String.valueOf(book.getYearOfPublication()));
            authorField.setText(book.authorsToString());
            genreField.setText(book.genresToString());
            copiesField.setText("0");
            descriptionField.setText(book.getShortDescription());
            setImage(imageView, book.getImage());
        }
    }

    private void setImage(ImageView imageView, Image image) {
        if (image != null) {
            Image cropped = ImageHandler.cropImageByRatio(image, 1, 1.5);
            imageView.setImage(cropped);
        }
    }

    @FXML
    private void goBack() {
        UI ui = UI.MANAGE_BOOK;
        MainController mainController = UIManager.getController(UI.MAIN);
        if (mainController.getCurrentTab() == ui) {
            return;
        }
        mainController.setCurrentTab(ui);
        mainController.navigate(UIManager.getRootOnce(ui));
        UIManager.getUpdatableController(ui).onUpdate();
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @FXML
    private void browseImage(ActionEvent event) {
        File file = ImageHandler.chooseImage("");
        if (file == null) {
            return;
        }
        newImageFile = file;
        Image loadedImage = ImageHandler.loadImageFromFile(file);
        setImage(imageView, loadedImage);
    }

    @FXML
    private void save(ActionEvent event) {
        PopupManager.confirm("Are you sure you want to save changes?", () -> {
            try {
                String newTitle = bookTitleField.getText();
                int newYearOfPublication = Integer.parseInt(yearField.getText());
                String newDescription = descriptionField.getText();
                int newNumberOfPages = book.getNumberOfPages();
                int copyDifference = Integer.parseInt(copiesField.getText());
                if (copyDifference < 0) {
                    throw new ValidationException("The copy difference must be positive");
                }
                BookService.getInstance().updateBook(book, newImageFile, newTitle,
                        newYearOfPublication, newDescription, newNumberOfPages, copyDifference);
                PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("update book"));
            } catch (NumberFormatException e) {
                PopupManager.info("Invalid number format");
            } catch (DatabaseException | ValidationException e) {
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }
}