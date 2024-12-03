package com.hxngxd.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ItemDisplayController {

    protected static final Logger log = LogManager.getLogger(ItemDisplayController.class);

    @FXML
    protected ImageView imageView;

    @FXML
    protected Label nameLabel;

    @FXML
    protected Label informationLabel;

    protected void setImageView(Image imageView) {
        this.imageView.setImage(imageView);
    }

    protected String getName() {
        return nameLabel.getText();
    }

    protected void setName(String name) {
        this.nameLabel.setText(name);
    }

    protected void setInformation(String information) {
        this.informationLabel.setText(information);
    }

}
