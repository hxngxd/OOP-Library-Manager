package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WelcomeController {
    @FXML
    private Label loginStatusLabel;
    @FXML
    private Label registerStatusLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private final UserService userService = UserService.getInstance();
    private final StageManager stageManager = StageManager.getInstance();

    @FXML
    private void goToLogin(ActionEvent event) {
        stageManager.setScene(UI.LOGIN);
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        stageManager.setScene(UI.REGISTER);
    }

    @FXML
    private void logIn(ActionEvent event) {
        userService.login(
                usernameField.getText(), usernameField.getText(), passwordField.getText());
    }

    @FXML
    private void register(ActionEvent event) {

    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}
