package com.hxngxd.ui.controller.tab;

import com.hxngxd.entities.User;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.scene.MainController;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public final class AccountController {

    private static final Logger log = LogManager.getLogger(AccountController.class);
    private final UserService userService = UserService.getInstance();

    private User currentUser = null;

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
        onActive();
        idField.setText(String.valueOf(currentUser.getId()));
        roleField.setText(currentUser.getRole().name());
        joinDateField.setText(Formatter.formatDateTime(currentUser.getDateAdded()));
        usernameField.setText(currentUser.getUsername());
        birthdayField.setStyle("-fx-font-size: 16px;");
    }

    @FXML
    private void browseImage(ActionEvent event) {
        File file = ImageHandler.chooseImage("");
        if (file == null) {
            return;
        }
        Image cropped = ImageHandler.cropImageByRatio(ImageHandler.loadImageFromFile(file), 1, 1);
        MainController.getInstance().setProfileImage(cropped);
        setProfileImage(cropped);
        UserService.getInstance().updateProfilePicture(file);
    }

    private void setProfileImage(Image profileImage) {
        ImageHandler.circleCrop(this.profileImage, 150);
        this.profileImage.setImage(profileImage);
    }

    public void onActive() {
        currentUser = userService.getCurrentUser();
        setProfileImage(ImageHandler.cropImageByRatio(currentUser.getImage(), 1, 1));
        lastNameField.setText(currentUser.getLastName());
        firstNameField.setText(currentUser.getFirstName());
        emailField.setText(currentUser.getEmail());
        addressField.setText(currentUser.getAddress());
        birthdayField.setValue(currentUser.getDateOfBirth());
        saveBookField.setText(String.valueOf(currentUser.getSavedBooks().size()));
        borrowBookField.setText(String.valueOf(0));
    }

    @FXML
    private void changePassword() {
        if (oldPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty()) {
            return;
        }
        PopupManager.confirm("Xác nhận đổi mật khẩu?", () -> {
            try {
                userService.changePassword(oldPasswordField.getText(), newPasswordField.getText());
                PopupManager.info("Đổi mật khẩu thành công!");
            } catch (DatabaseException | UserException e) {
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

    @FXML
    private void saveInformation() {
        PopupManager.confirm("Xác nhận lưu thông tin?", () -> {
            try {
                PopupManager.closePopup();
                changeEmail();
                changeProfile();
                PopupManager.info("Lưu thông tin thành công");
            } catch (DatabaseException | UserException | ValidationException e) {
                log.error(e.getMessage());
                PopupManager.info(e.getMessage());
            }
        });
    }

    private void changeEmail()
            throws DatabaseException, UserException, ValidationException {
        if (!emailField.getText().equals(currentUser.getEmail())) {
            userService.changeEmail(emailField.getText());
        }
    }

    private void changeProfile()
            throws DatabaseException, UserException, ValidationException {
        userService.updateProfile(firstNameField.getText(), lastNameField.getText(),
                birthdayField.getValue(), addressField.getText());
    }

    public static AccountController getInstance() {
        return UIManager.getControllerOnce(UI.ACCOUNT);
    }

}
