package com.hxngxd.ui.controller.popup;

import javafx.fxml.FXML;

public class ConfirmationPopupController extends InformationPopupController {

    private Runnable action;

    @FXML
    public void run() {
        if (action != null) {
            action.run();
        }
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
    
}
