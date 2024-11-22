package com.hxngxd.ui.controller.scene;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Genre;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public final class RegisterController extends AuthenticationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField confirmPasswordVisibleField;

    @Override
    @FXML
    protected void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setText(passwordVisibleField.getText());
            confirmPasswordField.setText(confirmPasswordVisibleField.getText());
            eye.setGlyphName("EYE_SLASH");
        } else {
            passwordVisibleField.setText(passwordField.getText());
            confirmPasswordVisibleField.setText(confirmPasswordField.getText());
            eye.setGlyphName("EYE");
        }
        passwordField.setVisible(isPasswordVisible);
        confirmPasswordVisibleField.setVisible(isPasswordVisible);
        passwordVisibleField.setVisible(!isPasswordVisible);
        confirmPasswordVisibleField.setVisible(!isPasswordVisible);
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    private void initialize() {
        confirmPasswordField.setOnKeyPressed(super::authenticateOnEnter);
        confirmPasswordVisibleField.setOnKeyPressed(super::authenticateOnEnter);
        onActive();
    }

    @Override
    public void onActive() {
        super.onActive();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        confirmPasswordField.clear();
        confirmPasswordVisibleField.clear();
    }

    @Override
    @FXML
    protected void authenticate(ActionEvent event) {
//        UserService userService = UserService.getInstance();
//        try {
//            String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
//            String confirmedPassword = isPasswordVisible ? confirmPasswordVisibleField.getText() : confirmPasswordField.getText();
//            userService.register(
//                    firstNameField.getText(), lastNameField.getText(),
//                    usernameField.getText(), emailField.getText(),
//                    password, confirmedPassword
//            );
//            PopupManager.info(LogMsg.General.SUCCESS.getMSG("create account"));
//
//            Author.initialize();
//            Genre.loadAll();
//            BookService.initialize();
//            UserService.initialize();
//            StageManager.getInstance().setScene(UI.MAIN);
//            MainController.getInstance().onActive();
//
//        } catch (DatabaseException | UserException | ValidationException e) {
////            e.printStackTrace();
//            log.error(e.getMessage());
//            PopupManager.info(e.getMessage());
//        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        StageManager.getInstance().setScene(UI.LOGIN);
        UIManager.getControllerOnce(UI.LOGIN).onActive();
    }

}
