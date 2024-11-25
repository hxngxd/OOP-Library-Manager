package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.Activable;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.MainController;
import com.hxngxd.utils.ImageHandler;
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
    private TextArea bookIdField;

    @FXML
    private TextArea bookTitleField;

    @FXML
    private TextArea yearField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField copiesField;

    @FXML
    private TextArea descriptionField;

    @Override
    public void onActive() {
        if (book != null) {
            // Set thông tin cho các trường từ book
            bookIdField.setText(String.valueOf(book.getId()));
            bookTitleField.setText(book.getTitle());
            yearField.setText(String.valueOf(book.getYearOfPublication()));
            authorField.setText(book.authorsToString());
            genreField.setText(book.genresToString());
            copiesField.setText(book.getAvailableCopies() + "/" + book.getTotalCopies());
            descriptionField.setText(book.getShortDescription());

            // Set image trực tiếp từ book.getImage()
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
        Image loadedImage = ImageHandler.loadImageFromFile(file);
        setImage(imageView, loadedImage);
    }

    @FXML
    private void save(ActionEvent event) {

    }
}