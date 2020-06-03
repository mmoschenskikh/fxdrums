package ru.spbstu.fxdrums.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.spbstu.fxdrums.model.Drum;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrumsController implements Initializable {

    private final Drum bass = Drum.BASS;
    private final Drum snare = Drum.SNARE;
    private final Drum hiHat = Drum.HI_HAT;
    private final Drum crash = Drum.CRASH;
    private final Drum ride = Drum.RIDE;
    private final Drum hTom = Drum.HIGH_TOM;
    private final Drum mTom = Drum.MEDIUM_TOM;
    private final Drum fTom = Drum.FLOOR_TOM;

    @FXML
    public ToggleGroup skin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        skin.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioMenuItem rmi = (RadioMenuItem) skin.getSelectedToggle();
            if (rmi.getText().equals("Light")) {
                System.out.println("light");
                //TODO
            } else {
                System.out.println("dark");
            }
        });
    }

    public void handleKeyPressed(KeyEvent ke) {
        switch (ke.getCode()) {
            case M:
                bass.makeSound();
                break;
            case J:
                snare.makeSound();
                break;
            case B:
                hiHat.makeSound();
                break;
            case Y:
                crash.makeSound();
                break;
            case U:
                hTom.makeSound();
                break;
            case I:
                mTom.makeSound();
                break;
            case O:
                ride.makeSound();
                break;
            case L:
                fTom.makeSound();
                break;
        }
    }

    public void onMixer() {
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("mixer.fxml")));
            stage.setScene(scene);
            stage.setTitle("Mixer");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExit() {
        Platform.exit();
    }

    public void onBassClicked() {
        bass.makeSound();
    }

    public void onSnareClicked() {
        snare.makeSound();
    }

    public void onHiHatClicked() {
        hiHat.makeSound();
    }

    public void onCrashClicked() {
        crash.makeSound();
    }
}
