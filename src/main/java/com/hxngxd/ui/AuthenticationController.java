package com.hxngxd.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class AuthenticationController extends NavigateController {

    @FXML
    protected TextField usernameField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected TextField passwordVisibleField;

    @FXML
    protected FontAwesomeIconView eye;

    protected boolean isPasswordVisible = false;

    @FXML
    protected abstract void togglePasswordVisibility();

    @Override
    public void onActive() {
        usernameField.clear();
        passwordField.clear();
        passwordVisibleField.clear();
    }

    @FXML
    protected abstract void authenticate(ActionEvent event);

    protected void authenticateOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            authenticate(null);
            event.consume();
        }
    }

}
