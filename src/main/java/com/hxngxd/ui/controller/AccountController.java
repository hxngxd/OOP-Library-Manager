package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.shape.Circle;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class AccountController {
    private final UserService userService = UserService.getInstance();
    private final User currentUser = userService.getCurrentUser();
    @FXML
    private ImageView profileImage;
    @FXML
    private TextField idField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField joinDateField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker birthdayField;
    @FXML
    private TextField oldPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField saveBookField;
    @FXML
    private TextField borrowBookField;
    @FXML
    private TextField violateField;

    @FXML
    private void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss, dd-MM-yyyy");
        idField.setText(String.valueOf(currentUser.getId()));
        roleField.setText(currentUser.getRole().name());
        joinDateField.setText(currentUser.getDateAdded().format(formatter));
        usernameField.setText(currentUser.getUsername());
        birthdayField.setStyle("-fx-font-size: 16px;");
        update();
    }

    @FXML
    private void browseImage(ActionEvent event) {
        File file = ImageHandler.chooseImage("");
        if (file == null) {
            return;
        }
        MainController mainController = UIManager.loadOnce(UI.MAIN).getController();
        Image cropped = ImageHandler.cropImageByRatio(
                ImageHandler.loadImageFromFile(file), 1, 1);
        mainController.setProfileImage(cropped);
        setProfileImage(cropped);
        UserService.getInstance().updateProfilePicture(file);
    }

    private void setProfileImage(Image profileImage) {
        this.profileImage.setFitHeight(150);
        this.profileImage.setFitWidth(150);
        this.profileImage.setPreserveRatio(true);
        Circle clip = new Circle(75, 75, 75);
        this.profileImage.setClip(clip);
        this.profileImage.setImage(profileImage);
    }

    public void update() {
        setProfileImage(ImageHandler.cropImageByRatio(
                currentUser.getImage(), 1, 1));
        lastNameField.setText(currentUser.getLastName());
        firstNameField.setText(currentUser.getFirstName());
        emailField.setText(currentUser.getEmail());
        addressField.setText(currentUser.getAddress());
        birthdayField.setValue(currentUser.getDateOfBirth());
        saveBookField.setText(String.valueOf(currentUser.getSavedBooks().size()));
        borrowBookField.setText(String.valueOf(0));
        violateField.setText(String.valueOf(currentUser.getViolationCount()));
    }
}
