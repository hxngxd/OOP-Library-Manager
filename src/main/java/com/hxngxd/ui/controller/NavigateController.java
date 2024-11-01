package com.hxngxd.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class NavigateController {
    @FXML
    protected void exit(ActionEvent event) {
        Platform.exit();
    }
}