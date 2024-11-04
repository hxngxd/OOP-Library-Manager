package com.hxngxd.ui.controller;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Genre;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
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

public class LoginController extends NavigateController {
    private static final Logger log = LogManager.getLogger(LoginController.class);
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField PasswordVisibleField;
    @FXML
    private FontAwesomeIconView icon;

    private boolean passWordVisible = true;

    @FXML
    public void togglePasswordVisibility() {
        if (passWordVisible) {
            PasswordVisibleField.setText(passwordField.getText());
            passwordField.setVisible(false);
            PasswordVisibleField.setVisible(true);
            passWordVisible = false;
            icon.setGlyphName("EYE");
        } else {
            passwordField.setVisible(true);
            PasswordVisibleField.setVisible(false);
            passwordField.setText(PasswordVisibleField.getText());
            icon.setGlyphName("EYE_SLASH");
            passWordVisible = true;
        }
    }


    private final UserService userService = UserService.getInstance();

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
        PasswordVisibleField.setOnKeyPressed(this::handleEnterKey);
    }


    @FXML
    private void logIn(ActionEvent event) {
        try {
            userService.login(
                    "23020078", "23020078",
                    "Hung@07112005"
            );
            statusLabel.setText(LogMessages.General.SUCCESS.getMessage("log in"));
            Author.initialize();
            Genre.initialize();
            BookService.initialize();
            UserService.initialize();
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