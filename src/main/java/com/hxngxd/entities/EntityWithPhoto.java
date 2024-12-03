package com.hxngxd.entities;

import javafx.scene.image.Image;

public abstract class EntityWithPhoto extends Entity {

    protected Image image = null;

    public EntityWithPhoto() {
    }

    public EntityWithPhoto(int id) {
        super(id);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
}
