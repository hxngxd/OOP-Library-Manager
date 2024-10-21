package com.hxngxd.ui;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.utils.ImageHandler;
import com.hxngxd.utils.LogMsg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Objects;

public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

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

    public static void init() {
        StageManager.getInstance().setScene(UI.HOME);
        BookService.init();
        HomeController controller = UIManager.Loaders.get(UI.HOME).getController();
        User user = UserService.getInstance().getCurrentUser();
        controller.displayBooks();
        controller.setFullNameLabel(user.getFullNameLastThenFirst());
        controller.setUsernameLabel(user.getUsername());
        controller.setRoleLabel(user.getRole());
        controller.setIdLabel(user.getId());
    }

    public void displayBooks() {
        Collections.shuffle(BookService.currentBooks);
        for (Book book : BookService.currentBooks) {
            try {
                VBox bookBox = (VBox) (Objects.requireNonNull(
                        UIManager.load(UI.BOOK_DISPLAY)).load());
                bookBox.setId(String.valueOf(book.getId()));
                ((Label) bookBox.lookup("#bookDisplayTitle")).setText(book.getTitle());
                ((Label) bookBox.lookup("#bookDisplayYear")).setText(
                        String.valueOf(book.getYearOfPublication())
                );
                ((Label) bookBox.lookup("#bookDisplayReview")).setText(
                        book.getAverageRating() == 0.0 ?
                                "Chưa được đánh giá" : String.valueOf(book.getAverageRating())
                );
                ((ImageView) bookBox.lookup("#bookDisplayCoverImage")).setImage(
                        ImageHandler.cropImageByRatio(book.getCoverImage(), 1, 1)
                );
                bookDisplayContainer.getChildren().add(bookBox);
            } catch (Exception e) {
                logger.error(LogMsg.fail("display book"), e);
            }
        }
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
        try {
            File file = ImageHandler.chooseImage("");
            if (file == null) {
                throw new Exception();
            }
            Image image = ImageHandler.cropImageByRatio(
                    Objects.requireNonNull(
                            ImageHandler.loadImageFromPath(file.getAbsolutePath())),
                    1, 1
            );
            profilePicture.setImage(image);
        } catch (Exception e) {
            logger.info(LogMsg.fail("loadOnce image"));
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}
