package com.hxngxd.ui.controller.popup;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public final class ConfirmationInputPopupController extends ConfirmationPopupController {

    @FXML
    private TextField textField;

    public String getText() {
        return textField.getText();
    }

    public static ConfirmationInputPopupController getInstance() {
        return UIManager.getControllerOnce(UI.CONFIRMATION_INPUT_POPUP);
    }
}
