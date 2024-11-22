package com.hxngxd.ui.controller.scene;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public final class LoginController extends AuthenticationController {

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

    @FXML
    private void initialize() {
        usernameField.setOnKeyPressed(super::authenticateOnEnter);
        passwordField.setOnKeyPressed(super::authenticateOnEnter);
        passwordVisibleField.setOnKeyPressed(super::authenticateOnEnter);
        onActive();
    }

    @Override
    @FXML
    protected void authenticate(ActionEvent event) {
//        UserService userService = UserService.getInstance();
//        try {
//            String username = usernameField.getText();
//            String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
////            userService.login(username, username, password);
//            if (username.equals("2")) {
//                userService.login("23020111", "23020111", "Minh@07092005");
//            } else {
//                userService.login("23020078", "23020078", "Hung@07112005");
//            }
//            PopupManager.info(LogMsg.General.SUCCESS.getMSG("log in"));
//
//            Author.initialize();
//            Genre.loadAll();
//            BookService.initialize();
//            UserService.initialize();
//
//            StageManager.getInstance().setScene(UI.MAIN);
//            MainController.getInstance().onActive();
//
//        } catch (Exception e) {
//            log.error(LogMsg.GENERAL_FAIL.msg("log in"), e);
//            PopupManager.info(e.getMessage());
//        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        StageManager.getInstance().setScene(UI.REGISTER);
        UIManager.getControllerOnce(UI.REGISTER).onActive();
    }

    @FXML
    private void forgotPassword() {
        PopupManager.info("Contact ADMIN to reset your password");
    }

}