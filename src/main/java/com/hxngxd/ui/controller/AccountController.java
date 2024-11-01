package com.hxngxd.ui.controller;


import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.File;

public class AccountController {

    @FXML
    private void browseImage(ActionEvent event) {
        File file = ImageHandler.chooseImage("");
        if (file == null) {
            return;
        }
        MainController mainController = UIManager.loadOnce(UI.MAIN).getController();
        mainController.setImage(
                ImageHandler.cropImageByRatio(
                        ImageHandler.loadImageFromFile(file), 1, 1));
        UserService.getInstance().updateProfilePicture(file);
    }
}
