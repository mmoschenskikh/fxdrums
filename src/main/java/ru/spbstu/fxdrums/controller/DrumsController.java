package ru.spbstu.fxdrums.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.spbstu.fxdrums.model.Drum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_FILE;
import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_MIDI;

public class DrumsController implements Initializable {

    private final String MIDI_TOGGLE_LABEL = "MIDI (a bit slower, platform-dependent)";
    private final String FILE_TOGGLE_LABEL = ".wav files (a bit faster, needs extra files)";


    private final Drum bass = Drum.BASS;
    private final Drum snare = Drum.SNARE;
    private final Drum hiHat = Drum.HI_HAT;
    private final Drum crash = Drum.CRASH;
    private final Drum ride = Drum.RIDE;
    private final Drum mTom = Drum.MEDIUM_TOM;
    private final Drum fTom = Drum.FLOOR_TOM;

    private final Drum[] drums = {bass, snare, hiHat, crash, ride, mTom, fTom};

    @FXML
    public ToggleGroup soundType;

    @FXML
    public RadioMenuItem typeMidi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        soundType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioMenuItem rmi = (RadioMenuItem) soundType.getSelectedToggle();
            if (rmi.getText().equals(MIDI_TOGGLE_LABEL)) {
                try {
                    setPlayerType(PLAYER_TYPE_MIDI);
                } catch (FileNotFoundException neverHappens) {
                    // Never happens
                }
            } else {
                try {
                    setPlayerType(PLAYER_TYPE_FILE);
                } catch (FileNotFoundException e) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("alertbox.fxml"));
                        Scene scene = new Scene(loader.load());
                        Stage stage = new Stage();

                        stage.setScene(scene);
                        stage.setTitle("Cannot find a sound file");
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);

                        AlertBoxController ab = loader.getController();
                        ab.setMessage(e.getMessage());

                        stage.showAndWait();
                    } catch (IOException exception) {
                        e.printStackTrace();
                    }
                    try {
                        setPlayerType(PLAYER_TYPE_MIDI);
                    } catch (FileNotFoundException neverHappens) {
                        // Never happens
                    }
                    typeMidi.setSelected(true);
                }
            }
        });
    }

    /**
     * Changes the player type for every single drum.
     *
     * @param playerType the type of sound player to set.
     * @throws FileNotFoundException if PLAYER_TYPE_FILE chosen and some file is not found.
     */
    private void setPlayerType(int playerType) throws FileNotFoundException {
        for (Drum drum : drums) {
            drum.setPlayer(playerType);
        }
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
