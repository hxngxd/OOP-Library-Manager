package com.hxngxd.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class PreviewController {
    @FXML
    protected ImageView image;
    @FXML
    protected Label nameLabel;
    @FXML
    protected Label informationLabel;

    protected void setImage(Image image) {
        this.image.setImage(image);
    }

    protected String getName() {
        return nameLabel.getText();
    }

    protected void setName(String name) {
        this.nameLabel.setText(name);
    }

    protected String getInformation() {
        return informationLabel.getText();
    }

    protected void setInformation(String information) {
        this.informationLabel.setText(information);
    }
}
