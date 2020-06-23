package ru.spbstu.fxdrums;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.spbstu.fxdrums.controller.DrumsController;

import java.io.IOException;

public class DrumsApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("drums.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("FXDrums");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        DrumsController dc = loader.getController();
        dc.setHostServices(getHostServices());

        stage.show();
    }

}