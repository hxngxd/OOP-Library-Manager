package com.hxngxd.libraryManager;

import com.hxngxd.database.DBManager;
import com.hxngxd.service.UserService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws IOException {
        boolean connect = DBManager.connect();
        if (!connect) {
            Platform.exit();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        UserService.loginByUsername("23020078", "Hung@07112005");
    }

    @Override
    public void stop() {
        UserService.logout();
        boolean disconnect = DBManager.disconnect();
    }

    public static void main(String[] args) {
        launch();
    }
}