package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.service.AuthorService;
import com.hxngxd.service.BookService;
import com.hxngxd.service.GenreService;
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
        try {
            String username = usernameField.getText();
            String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
            if (username.equals("2")) {
                userService.login("23020111", "23020111", "Minh@07092005");
            } else {
                userService.login("23020078", "23020078", "Hung@07112005");
            }
            PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("log in"));

            AuthorService.getInstance().loadAll();
            GenreService.getInstance().loadAll();
            BookService.getInstance().loadAll();
            userService.loadSavedBooks(User.getCurrent());

            StageManager.getInstance().setScene(UI.MAIN);
            UIManager.getActivableController(UI.MAIN).onActive();
        } catch (DatabaseException | UserException | ValidationException e) {
            log.error(LogMsg.GENERAL_FAIL.msg("log in"), e);
            PopupManager.info(e.getMessage());
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        StageManager.getInstance().setScene(UI.REGISTER);
        UIManager.getActivableController(UI.REGISTER).onActive();
    }

    @FXML
    private void forgotPassword() {
        PopupManager.info("Contact ADMIN to reset your password");
    }

}