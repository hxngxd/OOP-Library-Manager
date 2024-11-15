package com.hxngxd.ui.controller.scene;

import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class NavigateController {

    @FXML
    protected void exit(ActionEvent event) {
        PopupManager.confirm("Thoát chương trình?", () -> {
            PopupManager.closePopup();
            StageManager.closeMainStage();
        });
    }

}