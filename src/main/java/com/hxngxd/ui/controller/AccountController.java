package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;

import java.io.File;

public final class AccountController extends NavigateController {

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
    private void initialize() {
        User user = User.getCurrent();
        idField.setText(String.valueOf(user.getId()));
        roleField.setText(user.getRole().name());
        joinDateField.setText(Formatter.formatDateTime(user.getDateAdded()));
        usernameField.setText(user.getUsername());
        birthdayField.setStyle("-fx-font-size: 16px;");
        onActive();
    }

    @FXML
    private void browseImage(ActionEvent event) {
        File file = ImageHandler.chooseImage("");
        if (file == null) {
            return;
        }
        Image cropped = ImageHandler.cropImageByRatio(ImageHandler.loadImageFromFile(file), 1, 1);
        ((MainController) UIManager.getController(UI.MAIN)).setProfileImage(cropped);
        setProfileImage(cropped);
        userService.updateProfilePicture(file);
    }

    private void setProfileImage(Image profileImage) {
        ImageHandler.circleCrop(this.profileImage, 150);
        this.profileImage.setImage(profileImage);
    }

    @Override
    public void onActive() {
        User user = User.getCurrent();
        setProfileImage(ImageHandler.cropImageByRatio(user.getImage(), 1, 1));
        lastNameField.setText(user.getLastName());
        firstNameField.setText(user.getFirstName());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        birthdayField.setValue(user.getDateOfBirth());
        saveBookField.setText(String.valueOf(user.getSavedBooks().size()));
        borrowBookField.setText(String.valueOf(0));
    }

    @FXML
    private void changePassword() {
        if (oldPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty()) {
            PopupManager.info("Please enter password!");
            return;
        }
        PopupManager.confirm("Change password?", () -> {
            try {
                userService.changePassword(oldPasswordField.getText(), newPasswordField.getText());
                PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("change password"));
            } catch (DatabaseException | UserException e) {
                log.error(e);
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

    @FXML
    private void saveInformation() {
        PopupManager.confirm("Save profile?", () -> {
            try {
                PopupManager.closePopup();
                changeEmail();
                changeProfile();
                PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("save profile"));
            } catch (DatabaseException | UserException | ValidationException e) {
                log.error(e);
                PopupManager.info(e.getMessage());
            }
        });
    }

    private void changeEmail()
            throws DatabaseException, UserException, ValidationException {
        if (!emailField.getText().equals(User.getCurrent().getEmail())) {
            userService.changeEmail(emailField.getText());
        }
    }

    private void changeProfile()
            throws DatabaseException, UserException, ValidationException {
        userService.updateProfile(firstNameField.getText(), lastNameField.getText(),
                birthdayField.getValue(), addressField.getText());
    }

}
