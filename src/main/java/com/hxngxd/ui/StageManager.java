package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.utils.LogMsg;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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
        try {
            UIManager.load(ui);
            Scene currentScene = new Scene(UIManager.Loaders.get(ui).load());
            this.mainStage.setScene(currentScene);
            this.mainStage.show();
        } catch (IOException e) {
            logger.info(LogMsg.fail("load scene"), e);
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
