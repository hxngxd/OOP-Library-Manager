package com.hxngxd.ui.controller;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController extends NavigateController {
    private static final Logger log = LogManager.getLogger(LoginController.class);
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisibleField;
    @FXML
    private Label statusLabel;
    @FXML
    private FontAwesomeIconView eye;
    private boolean isPasswordVisible = false;
    private final UserService userService = UserService.getInstance();

    @FXML
    public void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setText(passwordVisibleField.getText());
            eye.setGlyphName("EYE_SLASH");
        } else {
            passwordVisibleField.setText(passwordField.getText());
            eye.setGlyphName("EYE");
        }
        passwordField.setVisible(isPasswordVisible);
        passwordVisibleField.setVisible(!isPasswordVisible);
        isPasswordVisible = !isPasswordVisible;
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            logIn(null);
            event.consume();
        }
    }

    @FXML
    private void initialize() {
        usernameField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);
        passwordVisibleField.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    private void logIn(ActionEvent event) {
        try {
            userService.login(
                    "23020078", "23020078",
                    "Hung@07112005"
            );
            statusLabel.setText(LogMessages.General.SUCCESS.getMessage("log in"));
            StageManager.getInstance().setScene(UI.MAIN);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        StageManager.getInstance().setScene(UI.REGISTER);
    }
}