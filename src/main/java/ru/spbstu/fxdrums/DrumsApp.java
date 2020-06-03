package ru.spbstu.fxdrums;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DrumsApp extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("drums.fxml")));
        stage.setScene(scene);
        stage.show();
    }

}