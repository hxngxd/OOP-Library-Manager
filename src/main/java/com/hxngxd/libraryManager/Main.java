package com.hxngxd.libraryManager;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;
import com.hxngxd.service.UserService;

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

    private static final Logger logger = LogManager.getLogger(Main.class);
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final UserService us = UserService.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        boolean connect = db.connect();
        if (!connect) {
            Platform.exit();
            return;
        }
        stage.setTitle("Library Manangement");
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent welcome = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("login.fxml")));
        Scene scene = new Scene(welcome);

        double widthRatio = 0.4;
        double heightRatio = 0.65;
        stage.setWidth(screenSize.getWidth() * widthRatio);
        stage.setHeight(screenSize.getHeight() * heightRatio);
        stage.setMinWidth(screenSize.getWidth() * widthRatio);
        stage.setMinHeight(screenSize.getHeight() * heightRatio);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        us.logout();
        boolean disconnect = db.disconnect();
    }

    public static void main(String[] args) {
        launch();
    }
}