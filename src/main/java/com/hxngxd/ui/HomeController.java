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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "hh:mm:ss, dd-MM-yyyy");

    public static void init() {
        StageManager.getInstance().setScene(UI.HOME);
        Author.init();
        Genre.init();
        BookService.init();
        HomeController controller = UIManager.Loaders.get(UI.HOME).getController();
        User user = UserService.getInstance().getCurrentUser();
        controller.displayBooks();
        if (user.getRole() != null) {
            controller.setProfilePicture(
                    ImageHandler.cropImageByRatio(user.getImage(), 1, 1)
            );
        }
        controller.setFullNameLabel(user.getFullNameLastThenFirst());
        controller.setUsernameLabel(user.getUsername());
        controller.setRoleLabel(user.getRole());
        controller.setIdLabel(user.getId());
        controller.loadPreviewBookContainer();
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
                updatePreviewBox(bookBox, book, false);
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
            updatePreviewBox(previewBox, book, true);
        }
    }

    private void updatePreviewBox(VBox previewBox, Book book, boolean addBulletPoint) {
        previewBox.setId(String.valueOf(book.getId()));

        String bullet = addBulletPoint ? "• " : "";

        ((Label) previewBox.lookup("#bookDisplayTitle")).setText(book.getTitle());

        ((Label) previewBox.lookup("#bookDisplayYear")).setText(
                bullet + (addBulletPoint ? "Năm xuất bản: " : "") + book.getYearOfPublication()
        );

        StringBuilder authors = new StringBuilder(bullet + (addBulletPoint ? "Tác giả: " : ""));
        for (int i = 0; i < book.getAuthors().size(); i++) {
            authors.append(book.getAuthors().get(i).getFullNameFirstThenLast());
            if (i < book.getAuthors().size() - 1) {
                authors.append(", ");
            }
        }
        ((Label) previewBox.lookup("#bookDisplayAuthor")).setText(authors.toString());

        StringBuilder genres = new StringBuilder(bullet + (addBulletPoint ? "Thể loại: " : ""));
        for (int i = 0; i < book.getGenres().size(); i++) {
            genres.append(book.getGenres().get(i).getName());
            if (i < book.getGenres().size() - 1) {
                genres.append(", ");
            }
        }
        ((Label) previewBox.lookup("#bookDisplayGenre")).setText(genres.toString());

        if (addBulletPoint) {
            TextFlow description = ((TextFlow) previewBox.lookup("#bookDescription"));
            if (description != null) {
                description.getChildren().clear();
                Text text = new Text(
                        bullet + "Mô tả ngắn: " + book.getShortDescription()
                );
                text.setFont(Font.font("System", 14));
                description.getChildren().add(text);
                description.setTextAlignment(TextAlignment.LEFT);
            }

            ((Label) previewBox.lookup("#bookDisplayPages")).setText(
                    bullet + "Số trang: " + String.valueOf(book.getNumberOfPages()));

            ((Label) previewBox.lookup("#bookDisplayDateAdded")).setText(
                    bullet + "Ngày thêm: " + book.getDateAdded().format(formatter)
            );

            ((Label) previewBox.lookup("#bookDisplayLastUpdated")).setText(
                    bullet + "Cập nhật gần nhất: " + book.getLastUpdated().format(formatter)
            );

            ((Label) previewBox.lookup("#bookDisplayCopies")).setText(
                    bullet + "Có sẵn: " + String.valueOf(book.getAvailableCopies()) + "/"
                            + String.valueOf(book.getTotalCopies()) + " bản"
            );
        }

        ((Label) previewBox.lookup("#bookDisplayReview")).setText(
                bullet + (addBulletPoint ? "Đánh giá: " : "") +
                        (book.getAverageRating() == 0.0
                                ? "Chưa được đánh giá"
                                : String.valueOf(book.getAverageRating()))
        );

        ImageView image = (ImageView) previewBox.lookup("#bookDisplayCoverImage");
        if (image != null) {
            image.setImage(
                    ImageHandler.cropImageByRatio(book.getImage(), 1, 1.5)
            );
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}
