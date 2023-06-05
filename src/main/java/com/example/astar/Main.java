// Author: Salah Tawafsha
// just run FXMLLoader.load() to load the fxml file
package com.example.astar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Shortest path finder");
        stage.getIcons().add(new Image("file:src/main/resources/com/example/astar/icon.png"));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}