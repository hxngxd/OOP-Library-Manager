package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StageManager {
    private static final Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private static final Logger logger = LogManager.getLogger(StageManager.class);

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

    public void setScene(UI ui) {
        UIManager.loadOnce(ui);
        this.mainStage.setScene(
                new Scene(UIManager.fxmlCache.get(ui))
        );
        this.mainStage.show();
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

    public Stage getMainStage() {
        return mainStage;
    }
}
