package com.hxngxd.ui;

import com.hxngxd.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class NavigateController implements Activable {

    protected static final Logger log = LogManager.getLogger(NavigateController.class);

    UserService userService = UserService.getInstance();

    @FXML
    protected void exit(ActionEvent event) {
        PopupManager.confirm("Exit now?", () -> {
            PopupManager.closePopup();
            StageManager.closeMainStage();
        });
    }

}