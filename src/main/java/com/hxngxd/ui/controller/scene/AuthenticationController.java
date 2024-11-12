package com.hxngxd.ui.controller.scene;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

}
