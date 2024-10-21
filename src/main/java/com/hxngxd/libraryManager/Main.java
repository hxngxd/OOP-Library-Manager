package com.hxngxd.libraryManager;

import com.hxngxd.ui.HomeController;
import com.hxngxd.ui.StageManager;
import com.hxngxd.database.DatabaseManager;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;

import com.hxngxd.ui.UIManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private final Logger logger = LogManager.getLogger(Main.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final UserService userService = UserService.getInstance();
    private final BookService bookService = BookService.getInstance();
    private final StageManager stageManager = StageManager.getInstance();


    @Override
    public void start(Stage stage) throws IOException {
        boolean connect = db.connect();
        if (!connect) {
            Platform.exit();
            return;
        }
        stageManager.init(stage);
//        HomeController homeController = UIManager.fxmlCache.get(UI.HOME).getController();
//        homeController.displayBooks();
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