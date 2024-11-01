package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class MainController extends NavigateController {
    private static final Logger log = LogManager.getLogger(MainController.class);
    @FXML
    private ImageView image;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label idLabel;
    @FXML
    private SplitPane root;

    @FXML
    private void initialize() {
        User user = UserService.getInstance().getCurrentUser();
        if (user.getImage() != null) {
            setImage(
                    ImageHandler.cropImageByRatio(user.getImage(), 1, 1)
            );
        }
        setFullNameLabel(user.getFullNameLastThenFirst());
        setUsernameLabel(user.getUsername());
        setRoleLabel(user.getRole());
        setIdLabel(user.getId());

        try {
            AnchorPane bookGallery = (AnchorPane) Objects.requireNonNull(
                    UIManager.loadOnce(UI.BOOK_GALLERY)).getRoot();
            root.getItems().add(bookGallery);
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.error(LogMessages.General.FAIL.getMessage("load book gallery"),
                    e.getMessage());
        }
    }

    public void setImage(Image image) {
        this.image.setImage(image);
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

    //    @FXML
//    private void browseImage(ActionEvent event) {
//        File file = ImageHandler.chooseImage("");
//        if (file == null) {
//            return;
//        }
//        this.image.setImage(
//                ImageHandler.cropImageByRatio(
//                        ImageHandler.loadImageFromFile(file), 1, 1));
//        UserService.getInstance().updateProfilePicture(file);
//    }
}
