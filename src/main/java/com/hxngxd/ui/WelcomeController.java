package com.hxngxd.ui;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
//            userService.login(
//                    usernameField.getText(), usernameField.getText(),
//                    passwordField.getText()
//            );
            userService.login(
                    "23020078", "23020078",
                    "Hung@07112005"
            );
            loginStatusLabel.setText(LogMessages.General.SUCCESS.getMessage("log in"));
            HomeController.init();
        } catch (Exception e) {
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