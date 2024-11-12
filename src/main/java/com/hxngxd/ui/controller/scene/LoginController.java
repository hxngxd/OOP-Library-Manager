package com.hxngxd.ui.controller.scene;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Genre;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.tab.BookGalleryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LoginController extends AuthenticationController {

    private static final Logger log = LogManager.getLogger(LoginController.class);

    @Override
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

    private void authenticateOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            logIn(null);
            event.consume();
        }
    }

    @FXML
    private void initialize() {
        usernameField.setOnKeyPressed(this::authenticateOnEnter);
        passwordField.setOnKeyPressed(this::authenticateOnEnter);
        passwordVisibleField.setOnKeyPressed(this::authenticateOnEnter);
        onActive();
    }

    public void onActive() {
        usernameField.setText("");
        passwordField.setText("");
        passwordVisibleField.setText("");
    }

    @FXML
    private void logIn(ActionEvent event) {
        UserService userService = UserService.getInstance();
        try {
            String username = usernameField.getText();
            String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
//            userService.login(username, username, password);
            if (username.equals("2")) {
                userService.login("23020111", "23020111", "Minh@07092005");
            } else {
                userService.login("23020078", "23020078", "Hung@07112005");
            }
            StageManager.showInformationPopup(LogMessages.General.SUCCESS.getMSG("log in"));

            Author.initialize();
            Genre.initialize();
            BookService.initialize();
            UserService.initialize();

            StageManager.getInstance().setScene(UI.MAIN);
            MainController.getInstance().onActive();

        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage());
            StageManager.showInformationPopup(e.getMessage());
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        StageManager.getInstance().setScene(UI.REGISTER);
        RegisterController.getInstance().onActive();
    }

    public static LoginController getInstance() {
        return UIManager.getControllerOnce(UI.LOGIN);
    }

}