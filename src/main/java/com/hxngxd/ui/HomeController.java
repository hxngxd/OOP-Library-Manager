package com.hxngxd.ui;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.ImageHandler;
import com.hxngxd.enums.LogMessages;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collections;
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
    private AnchorPane previewBookContainer;

    public static void init() {
        StageManager.getInstance().setScene(UI.HOME);
        BookService.init();
        HomeController controller = UIManager.Loaders.get(UI.HOME).getController();
        User user = UserService.getInstance().getCurrentUser();
        controller.displayBooks();
        if (user.getRole() != null) {
            controller.setProfilePicture(
                    ImageHandler.cropImageByRatio(user.getPhoto(), 1, 1)
            );
        }
        controller.setFullNameLabel(user.getFullNameLastThenFirst());
        controller.setUsernameLabel(user.getUsername());
        controller.setRoleLabel(user.getRole());
        controller.setIdLabel(user.getId());
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
        this.idLabel.setText("ID Number: " + String.valueOf(id));
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

    public void displayBooks() {
        splitPane.getItems().remove(previewBookContainer);
        Collections.shuffle(BookService.currentBooks);
        for (Book book : BookService.currentBooks) {
            try {
                VBox bookBox = (VBox) (Objects.requireNonNull(
                        UIManager.load(UI.BOOK_DISPLAY)).load());
                updatePreviewBox(bookBox, book);
                bookBox.setOnMouseClicked(mouseEvent -> previewBook(book));
                bookDisplayContainer.getChildren().add(bookBox);
            } catch (Exception e) {
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
            VBox previewBox = (VBox) previewBookContainer.getChildren().getFirst(); // lấy phần tử đầu tiên
            updatePreviewBox(previewBox, book);
        } else {
            if (splitPane.getItems().contains(previewBookContainer)) {
                splitPane.getItems().remove(previewBookContainer);
            } else {
                splitPane.getItems().add(previewBookContainer);
            }
        }
    }

    private void updatePreviewBox(VBox previewBox, Book book) {
        previewBox.setId(String.valueOf(book.getId()));
        ((Label) previewBox.lookup("#bookDisplayTitle")).setText(book.getTitle());
        ((Label) previewBox.lookup("#bookDisplayYear")).setText(String.valueOf(book.getYearOfPublication()));
        Label description = ((Label) previewBox.lookup("#bookDescription"));
        if (description != null) {
            description.setText("\tMô tả ngắn: " + book.getShortDescription());
        }
        ((Label) previewBox.lookup("#bookDisplayReview")).setText(
                book.getAverageRating() == 0.0 ? "Chưa được đánh giá" : String.valueOf(book.getAverageRating())
        );
        ((ImageView) previewBox.lookup("#bookDisplayCoverImage")).setImage(
                ImageHandler.cropImageByRatio(book.getCoverImage(), 1, 1.5)
        );
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}
