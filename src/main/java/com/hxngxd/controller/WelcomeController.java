package com.hxngxd.controller;

import com.hxngxd.entities.User;
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
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private final UserService us = UserService.getInstance();
    private final StageManager stageManager = StageManager.getInstance();

    @FXML
    void logIn(ActionEvent event) {
        us.login(usernameField.getText(), usernameField.getText(), passwordField.getText());
    }

    @FXML
    void goToRegister(ActionEvent event) {
        stageManager.setScene(SceneType.REGISTER);
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }
}
