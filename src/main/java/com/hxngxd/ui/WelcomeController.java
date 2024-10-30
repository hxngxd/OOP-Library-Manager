package com.hxngxd.ui;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WelcomeController {
    private static final Logger log = LogManager.getLogger(WelcomeController.class);
    @FXML
    private Label loginStatusLabel;
    @FXML
    private Label registerStatusLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

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
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        StageManager.getInstance().setScene(UI.LOGIN);
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        StageManager.getInstance().setScene(UI.REGISTER);
    }

    @FXML
    private void logIn(ActionEvent event) {
        try {
            userService.login(
                    "23020078", "23020078",
                    "Hung@07112005"
            );
            loginStatusLabel.setText(LogMessages.General.SUCCESS.getMessage("log in"));
            StageManager.getInstance().setScene(UI.HOME);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            loginStatusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void register(ActionEvent event) {

    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}