package com.hxngxd.controller;

import com.hxngxd.enums.SceneType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StageManager {
    private static final SceneManager sceneManager = SceneManager.getInstance();
    private static final Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private Stage mainStage;

    private StageManager() {
    }

    private static class SingletonHolder {
        private static final StageManager instance = new StageManager();
    }

    public static StageManager getInstance() {
        return SingletonHolder.instance;
    }

    public void init(Stage stage) {
        if (this.mainStage == null) {
            this.mainStage = stage;
        }
    }

    public void setScene(SceneType sceneType) {
        Scene currentScene = sceneManager.loadScene(sceneType);
        if (currentScene != null) {
            this.mainStage.setScene(currentScene);
            this.mainStage.show();
        }
    }

    public void setWidth(double minRatio, double prefRatio) {
        this.mainStage.setWidth(screenSize.getWidth() * prefRatio);
        this.mainStage.setMinWidth(screenSize.getWidth() * minRatio);
    }

    public void setHeight(double minRatio, double prefRatio) {
        this.mainStage.setHeight(screenSize.getHeight() * prefRatio);
        this.mainStage.setMinHeight(screenSize.getHeight() * minRatio);
    }

    public void setTitle(String title) {
        this.mainStage.setTitle(title);
    }
}
