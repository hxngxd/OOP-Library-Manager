package com.hxngxd.ui.controller.scene;

import com.hxngxd.ui.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class NavigateController {

    @FXML
    protected void exit(ActionEvent event) {
        StageManager.showConfirmationPopup("THOÁT CHƯƠNG TRÌNH?", StageManager::closeMainStage);
    }

}