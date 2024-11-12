package com.hxngxd.ui.controller.popup;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.scene.MainController;
import com.hxngxd.ui.controller.tab.ManageUserController;
import javafx.fxml.FXML;

public class ManagePopupController extends PopupController {

    @FXML
    private void manageUser() {
        MainController mainController = MainController.getInstance();
        UI ui = UI.MANAGE_USER;
        if (mainController.getCurrentTab() != ui) {
            mainController.setCurrentTab(ui);
            mainController.navigate(UIManager.getRootOnce(ui));
            ManageUserController.getInstance().loadUsers();
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