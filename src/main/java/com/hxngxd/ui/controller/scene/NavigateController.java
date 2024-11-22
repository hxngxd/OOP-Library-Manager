package com.hxngxd.ui.controller.scene;

import com.hxngxd.ui.Activable;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.Updatable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class NavigateController implements Activable {

    protected static final Logger log = LogManager.getLogger(NavigateController.class);

    @FXML
    protected void exit(ActionEvent event) {
        PopupManager.confirm("Exit now?", () -> {
            PopupManager.closePopup();
            StageManager.closeMainStage();
        });
    }

}