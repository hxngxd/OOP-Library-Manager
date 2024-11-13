package com.hxngxd.ui.controller.popup;

import com.hxngxd.ui.StageManager;
import javafx.fxml.FXML;

public abstract class PopupController {

    @FXML
    protected void exit() {
        StageManager.closePopup();
    }

}