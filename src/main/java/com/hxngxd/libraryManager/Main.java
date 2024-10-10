package com.hxngxd.libraryManager;

import com.hxngxd.database.DBManager;
import com.hxngxd.service.UserService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        boolean connect = DBManager.connect();
        UserService.loginByUsername("23020078", "Hung@07112005");
    }

    @Override
    public void stop(){
        UserService.logout();
        boolean disconnect = DBManager.disconnect();
    }

    public static void main(String[] args) {
        launch();
    }
}