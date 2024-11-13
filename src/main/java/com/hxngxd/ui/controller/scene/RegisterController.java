package com.hxngxd.ui.controller.scene;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Genre;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.exceptions.ValidationException;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RegisterController extends AuthenticationController {

    private static final Logger log = LogManager.getLogger(RegisterController.class);

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
    }

    public void onActive() {
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        passwordVisibleField.setText("");
        confirmPasswordField.setText("");
        confirmPasswordVisibleField.setText("");
    }

    @FXML
    private void register(ActionEvent event) {
        UserService userService = UserService.getInstance();
        try {
            String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
            String confirmedPassword = isPasswordVisible ? confirmPasswordVisibleField.getText() : confirmPasswordField.getText();
            userService.register(
                    firstNameField.getText(), lastNameField.getText(),
                    usernameField.getText(), emailField.getText(),
                    password, confirmedPassword
            );
            StageManager.showInfoPopup(LogMessages.General.SUCCESS.getMSG("create account"));

            Author.initialize();
            Genre.initialize();
            BookService.initialize();
            UserService.initialize();
            StageManager.getInstance().setScene(UI.MAIN);
            MainController.getInstance().onActive();

        } catch (DatabaseException | UserException | ValidationException e) {
//            e.printStackTrace();
            log.error(e.getMessage());
            StageManager.showInfoPopup(e.getMessage());
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        StageManager.getInstance().setScene(UI.LOGIN);
        LoginController.getInstance().onActive();
    }

    public static RegisterController getInstance() {
        return UIManager.getControllerOnce(UI.REGISTER);
    }

}
