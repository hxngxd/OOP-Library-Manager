package com.hxngxd.ui.controller.book;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private ComboBox<String> authorMenu;

    @FXML
    private TextField genreField;

    @FXML
    private ComboBox<String> genreMenu;

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

        ObservableList<String> authorData = FXCollections.observableArrayList();
        for (Author author : Author.authorSet) {
            authorData.add(author.getFullNameFirstThenLast());
        }
        authorMenu.setItems(authorData);

        List<Author> selectedAuthors = new ArrayList<>();
        authorMenu.setOnAction(event -> {
            String selectedAuthor = authorMenu.getValue();
            if (selectedAuthor != null) {
                Author author = Author.authorSet.stream()
                        .filter(a -> a.getFullNameFirstThenLast().equals(selectedAuthor))
                        .findFirst()
                        .orElse(null);
                if (author != null && !selectedAuthors.contains(author)) {
                    selectedAuthors.add(author);
                    updateAuthorField(selectedAuthors);
                    authorField.positionCaret(authorField.getText().length());
                }
            }
        });

        authorField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE && !selectedAuthors.isEmpty()) {
                selectedAuthors.removeLast();
                updateAuthorField(selectedAuthors);
                authorField.positionCaret(authorField.getText().length());
            } else {
                event.consume();
            }
        });

        ObservableList<String> genreData = FXCollections.observableArrayList();
        for (Genre genre : Genre.genreMap.values()) {
            genreData.add(genre.getName());
        }
        genreMenu.setItems(genreData);

        List<Genre> selectedGenres = new ArrayList<>();
        genreMenu.setOnAction(event -> {
            String selectedGenre = genreMenu.getValue();
            if (selectedGenre != null) {
                Genre genre = Genre.genreMap.values().stream()
                        .filter(g -> g.getName().equals(selectedGenre))
                        .findFirst()
                        .orElse(null);
                if (genre != null && !selectedGenres.contains(genre)) {
                    selectedGenres.add(genre);
                    updateGenreField(selectedGenres);
                    genreField.positionCaret(genreField.getText().length());
                }
            }
        });

        genreField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE && !selectedGenres.isEmpty()) {
                selectedGenres.removeLast();
                updateGenreField(selectedGenres);
                genreField.positionCaret(genreField.getText().length());
            } else {
                event.consume();
            }
        });
    }

    private void updateAuthorField(List<Author> selectedAuthors) {
        StringBuilder authorsText = new StringBuilder();
        for (Author author : selectedAuthors) {
            if (!authorsText.isEmpty()) {
                authorsText.append(", ");
            }
            authorsText.append(author.getFullNameFirstThenLast());
        }
        authorField.setText(authorsText.toString());
        authorField.positionCaret(authorField.getText().length());
    }

    private void updateGenreField(List<Genre> selectedGenres) {
        StringBuilder genresText = new StringBuilder();
        for (Genre genre : selectedGenres) {
            if (!genresText.isEmpty()) {
                genresText.append(", ");
            }
            genresText.append(genre.getName());
        }
        genreField.setText(genresText.toString());
        genreField.positionCaret(genreField.getText().length());
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