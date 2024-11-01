package com.hxngxd.ui.controller;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private void register(ActionEvent event) {
        log.info(firstNameField.getText());
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        StageManager.getInstance().setScene(UI.LOGIN);
    }
}
