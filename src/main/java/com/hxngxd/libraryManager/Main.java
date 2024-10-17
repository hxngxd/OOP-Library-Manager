package com.hxngxd.libraryManager;

import com.hxngxd.controller.StageManager;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.SceneType;
import com.hxngxd.service.UserService;

import com.hxngxd.utils.LogMsg;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private final Logger logger = LogManager.getLogger(Main.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final UserService userService = UserService.getInstance();
    private final StageManager stageManager = StageManager.getInstance();
    private final double widthRatio = 0.5;
    private final double heightRatio = 0.7;

    @Override
    public void start(Stage stage) throws IOException {
        boolean connect = db.connect();
        if (!connect) {
            Platform.exit();
            return;
        }
        stageManager.init(stage);
        stageManager.setTitle("Library Management");
        stageManager.setWidth(widthRatio, widthRatio);
        stageManager.setHeight(heightRatio, heightRatio);
        stageManager.setScene(SceneType.LOGIN);
    }

    @Override
    public void stop() {
        userService.logout();
        boolean disconnect = db.disconnect();
    }

    public static void main(String[] args) {
        launch();
    }
}