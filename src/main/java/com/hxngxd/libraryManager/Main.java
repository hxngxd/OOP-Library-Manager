package com.hxngxd.libraryManager;

import com.hxngxd.database.DBManager;
import com.hxngxd.entities.User;
import com.hxngxd.service.UserService;

import com.hxngxd.utils.EmailValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        boolean connect = DBManager.connect();
        UserService.register(
                "Hxngxd",
                "Nguyen",
                LocalDate.of(2005, 11, 7),
                "hngxd",
                "hunguong05@gmail.com",
                "quang ninh",
                "07112005", "07112005"
        );
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