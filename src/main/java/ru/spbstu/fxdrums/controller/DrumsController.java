package ru.spbstu.fxdrums.controller;

import javafx.application.HostServices;
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
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_FILE;
import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_MIDI;

public class DrumsController implements Initializable {

    private final Drum bass = Drum.BASS;
    private final Drum snare = Drum.SNARE;
    private final Drum hiHat = Drum.HI_HAT;
    private final Drum crash = Drum.CRASH;
    private final Drum ride = Drum.RIDE;
    private final Drum mTom = Drum.MEDIUM_TOM;
    private final Drum fTom = Drum.FLOOR_TOM;

    @FXML
    public RadioMenuItem typeMidi, typeFile;
    private HostServices services;

    private Drum[] drums = {bass, snare, hiHat, crash, ride, mTom, fTom};
    private boolean showingHelp = false;

    @FXML
    public ToggleGroup soundType;
    private String gitHubLink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PropertyResourceBundle bundle = new PropertyResourceBundle(getClass().getResourceAsStream("drums_strings"));
            typeMidi.setText(bundle.getString("midi_toggle_label"));
            typeFile.setText(bundle.getString("file_toggle_label"));
            gitHubLink = bundle.getString("github_link");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        soundType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (typeMidi.isSelected()) {
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
            drum.setPlayerType(playerType);
        }
    }

    public void onHelp() {
        try {
            if (!showingHelp) {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("help.fxml")));
                Stage stage = new Stage();

                stage.setScene(scene);
                stage.setTitle("Help");
                stage.setResizable(false);

                showingHelp = true;
                stage.showAndWait();
                showingHelp = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGitHub() {
        services.showDocument(gitHubLink);
    }

    public void setHostServices(HostServices services) {
        this.services = services;
    }

    public void handleKeyPressed(KeyEvent ke) {
        switch (ke.getCode()) {
            case B:
                onBass();
                break;
            case V:
                onSnare();
                break;
            case C:
                onHiHat();
                break;
            case D:
                onCrash();
                break;
            case H:
                onMTom();
                break;
            case J:
                onRide();
                break;
            case N:
                onFTom();
                break;
        }
    }

    public void onMixer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mixer.fxml"));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();

            MixerController mc = loader.getController();
            mc.setMixerValues(drums);

            stage.setScene(scene);
            stage.setTitle("Mixer");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnCloseRequest(event -> drums = mc.getMixerValues());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExit() {
        Platform.exit();
    }

    public void onBass() {
        bass.makeSound();
    }

    public void onSnare() {
        snare.makeSound();
    }

    public void onHiHat() {
        hiHat.makeSound();
    }

    public void onCrash() {
        crash.makeSound();
    }

    public void onMTom() {
        mTom.makeSound();
    }

    public void onRide() {
        ride.makeSound();
    }

    public void onFTom() {
        fTom.makeSound();
    }
}
