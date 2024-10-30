package com.hxngxd.ui;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.ImageHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeController {

    private static final Logger log = LogManager.getLogger(HomeController.class);

    @FXML
    private FlowPane bookDisplayContainer;

    @FXML
    private ImageView profilePicture;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label idLabel;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane previewBookContainer;

    @FXML
    private void initialize() {
        Author.initialize();
        Genre.initialize();
        BookService.initialize();
        User user = UserService.getInstance().getCurrentUser();
        displayBooks();
        if (user.getRole() != null) {
            setProfilePicture(
                    ImageHandler.cropImageByRatio(user.getImage(), 1, 1)
            );
        }
        setFullNameLabel(user.getFullNameLastThenFirst());
        setUsernameLabel(user.getUsername());
        setRoleLabel(user.getRole());
        setIdLabel(user.getId());
        loadPreviewBookContainer();
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture.setImage(profilePicture);
    }

    public void setFullNameLabel(String fullname) {
        this.fullNameLabel.setText(fullname);
    }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText("Username: " + username);
    }

    public void setRoleLabel(Role role) {
        this.roleLabel.setText("Role: " + role.name());
    }

    public void setIdLabel(int id) {
        this.idLabel.setText("ID Number: " + id);
    }

    @FXML
    private void browseImage(ActionEvent event) {
        File file = ImageHandler.chooseImage("");
        if (file == null) {
            return;
        }
        profilePicture.setImage(
                ImageHandler.cropImageByRatio(
                        ImageHandler.loadImageFromFile(file), 1, 1));
        UserService.getInstance().updateProfilePicture(file);
    }

    public void loadPreviewBookContainer() {
        try {
            this.previewBookContainer = Objects.requireNonNull(
                    UIManager.load(UI.BOOK_PREVIEW)).load();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(LogMessages.General.FAIL.getMessage("load preview container"), e);
        }
    }

    public void displayBooks() {
        List<Book> bookList = new ArrayList<>(Book.bookMap.values());
        Collections.shuffle(bookList);
        for (Book book : bookList) {
            try {
                VBox bookBox = Objects.requireNonNull(
                        UIManager.load(UI.BOOK_DISPLAY)).load();
                ImageView image = (ImageView) bookBox.lookup("#bookCoverImage");
                if (image != null) {
                    image.setImage(
                            ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
                    );
                }
                ((Label) bookBox.lookup("#bookTitle")).setText(book.getTitle());
                ((Label) bookBox.lookup("#bookInformation")).setText(book.toString());
                bookBox.setOnMouseClicked(mouseEvent -> previewBook(book));
                bookDisplayContainer.getChildren().add(bookBox);
            } catch (IOException e) {
                e.printStackTrace();
                log.error(LogMessages.General.FAIL.getMessage("display book"), e);
            }
        }
    }

    private void previewBook(Book book) {
        BookService bookService = BookService.getInstance();
        if (bookService.getCurrentBook() == null || !bookService.getCurrentBook().equals(book)) {
            if (!splitPane.getItems().contains(previewBookContainer)) {
                splitPane.getItems().add(previewBookContainer);
            }
            bookService.setCurrentBook(book);
            VBox previewBox = (VBox)
                    ((AnchorPane) previewBookContainer.getContent()).getChildren().getFirst();
            ImageView image = (ImageView) previewBox.lookup("#bookCoverImage");
            if (image != null) {
                image.setImage(
                        ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
                );
            }
            ((Label) previewBox.lookup("#bookTitle")).setText(book.getTitle());
            ((Label) previewBox.lookup("#bookInformation")).setText(book.toStringDetail());
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}
