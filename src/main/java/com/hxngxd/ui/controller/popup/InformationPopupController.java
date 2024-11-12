package com.hxngxd.ui.controller.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InformationPopupController extends PopupController {

    @FXML
    private Label informationLabel;

    public void setInformationLabel(String text) {
        informationLabel.setText(text);
    }
    
}