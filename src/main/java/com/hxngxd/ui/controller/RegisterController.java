package com.hxngxd.ui.controller;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Genre;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterController extends NavigateController {
    private static final Logger log = LogManager.getLogger(RegisterController.class);
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField passwordVisibleField;
    @FXML
    private TextField confirmPasswordVisibleField;
    @FXML
    private FontAwesomeIconView eyePassword;

    private final UserService userService = UserService.getInstance();
    private boolean isPasswordVisible = false;

    @FXML
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setText(passwordVisibleField.getText());
            confirmPasswordField.setText(confirmPasswordVisibleField.getText());
            eyePassword.setGlyphName("EYE_SLASH");
        } else {
            passwordVisibleField.setText(passwordField.getText());
            confirmPasswordVisibleField.setText(confirmPasswordField.getText());
            eyePassword.setGlyphName("EYE");
        }
        passwordField.setVisible(isPasswordVisible);
        confirmPasswordVisibleField.setVisible(isPasswordVisible);
        passwordVisibleField.setVisible(!isPasswordVisible);
        confirmPasswordVisibleField.setVisible(!isPasswordVisible);
        isPasswordVisible = !isPasswordVisible;
    }


    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            register(null);
            event.consume();
        }
    }

    @FXML
    private void initialize() {
        confirmPasswordField.setOnKeyPressed(this::handleEnterKey);
        confirmPasswordVisibleField.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    private void register(ActionEvent event) {
        try {
            userService.register(
                    firstNameField.getText(), lastNameField.getText(),
                    usernameField.getText(), emailField.getText(),
                    passwordField.getText(), confirmPasswordField.getText()
            );
            statusLabel.setText(LogMessages.General.SUCCESS.getMessage("register"));
            Author.initialize();
            Genre.initialize();
            BookService.initialize();
            UserService.initialize();
            StageManager.getInstance().setScene(UI.MAIN);
        } catch (DatabaseException | UserException | ValidationException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        StageManager.getInstance().setScene(UI.LOGIN);
    }
}
