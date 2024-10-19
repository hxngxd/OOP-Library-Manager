package com.hxngxd.controller;

import com.hxngxd.enums.SceneType;
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
    void goToLogin(ActionEvent event) {
        stageManager.setScene(SceneType.LOGIN);
    }

    @FXML
    void goToRegister(ActionEvent event) {
        stageManager.setScene(SceneType.REGISTER);
    }

    @FXML
    void logIn(ActionEvent event) {
        userService.login(usernameField.getText(), usernameField.getText(), passwordField.getText());
    }

    @FXML
    void register(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }
}
