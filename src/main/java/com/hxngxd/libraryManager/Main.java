package com.hxngxd.libraryManager;

import com.hxngxd.ui.StageManager;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.service.UserService;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private final Logger log = LogManager.getLogger(Main.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final UserService userService = UserService.getInstance();
    private final StageManager stageManager = StageManager.getInstance();


    @Override
    public void start(Stage stage) throws IOException {
        if (!db.connect()) {
            Platform.exit();
            return;
        }
        stageManager.init(stage);
    }

    @Override
    public void stop() {
        try {
            userService.logout();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
        db.disconnect();
    }

    public static void main(String[] args) {
        launch();
    }
}