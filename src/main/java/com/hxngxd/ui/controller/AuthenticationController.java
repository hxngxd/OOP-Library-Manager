package com.hxngxd.ui.controller;

import com.hxngxd.actions.Borrowing;
import com.hxngxd.entities.User;
import com.hxngxd.enums.UI;
import com.hxngxd.service.*;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
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
    protected void authenticate(ActionEvent event) {
        AuthorService.getInstance().loadAll();
        GenreService.getInstance().loadAll();
        BookService.getInstance().loadAll();
        UserService.getInstance().loadAll();
        BookService.getInstance().setAllReviews();
        userService.loadSavedBooks(User.getCurrent());
        BorrowService.getInstance().loadAll();

        StageManager.getInstance().setScene(UI.MAIN);
        UIManager.getActivableController(UI.MAIN).onActive();
    }

    protected void authenticateOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            authenticate(null);
            event.consume();
        }
    }

}
