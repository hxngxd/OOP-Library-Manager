package com.hxngxd.ui.controller;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class ManagePopupController {
    @FXML
    private void exit() {
        StageManager.getInstance().closePopupStage();
    }

    @FXML
    private void manageUser() {
        MainController mainController = (MainController)
                UIManager.loadOnce(UI.MAIN).getController();
        if (mainController.getCurrentTab() != UI.MANAGE_USER) {
            mainController.setCurrentTab(UI.MANAGE_USER);
            FXMLLoader loader = UIManager.loadOnce(UI.MANAGE_USER);
            mainController.navigate(loader.getRoot());
        }
        exit();
    }

    @FXML
    private void manageBook() {
        exit();
    }

    @FXML
    private void manageBorrowBook() {
        exit();
    }
}