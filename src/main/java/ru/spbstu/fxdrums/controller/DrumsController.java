package ru.spbstu.fxdrums.controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.spbstu.fxdrums.model.main.Drum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static ru.spbstu.fxdrums.model.main.Player.PLAYER_TYPE_FILE;
import static ru.spbstu.fxdrums.model.main.Player.PLAYER_TYPE_MIDI;

public class DrumsController implements Initializable {

    private final Drum bass = Drum.BASS;
    private final Drum snare = Drum.SNARE;
    private final Drum hiHat = Drum.HI_HAT;
    private final Drum crash = Drum.CRASH;
    private final Drum ride = Drum.RIDE;
    private final Drum mTom = Drum.MEDIUM_TOM;
    private final Drum fTom = Drum.FLOOR_TOM;

    private HostServices services;
    private Drum[] drums = {bass, snare, hiHat, crash, ride, mTom, fTom};
    private String gitHubLink;


    // Boolean values to prevent opening more than one instance of window.
    private boolean showingHelp = false;
    private boolean showingMixer = false;
    private boolean showingMachine = false;
    // Stage objects to change focus if the window is already opened.
    private Stage mixerStage, helpStage, machineStage;

    @FXML
    public ToggleGroup soundType;
    @FXML
    public RadioMenuItem typeMidi, typeFile;
    @FXML
    public ImageView bassImage, snareImage, hiHatImage, crashImage, mTomImage, fTomImage, rideImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImages();

        try {
            PropertyResourceBundle bundle = new PropertyResourceBundle(getClass().getResourceAsStream("strings/drums_strings"));
            typeMidi.setText(bundle.getString("midi_toggle_label"));
            typeFile.setText(bundle.getString("file_toggle_label"));
            gitHubLink = bundle.getString("github_link");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        soundType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (typeMidi.isSelected())
                setPlayerType(PLAYER_TYPE_MIDI);
            else
                setPlayerType(PLAYER_TYPE_FILE);
        });
    }

    /**
     * Changes the player type for every single drum.
     * If PLAYER_TYPE_FILE was chosen and some of the files were not found, PLAYER_TYPE_MIDI is set.
     *
     * @param playerType the type of sound player to set.
     */
    private void setPlayerType(int playerType) {
        try {
            for (Drum drum : drums) {
                drum.setPlayerType(playerType);
            }
        } catch (FileNotFoundException e) {
            setPlayerType(PLAYER_TYPE_MIDI);
            typeMidi.setSelected(true);
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
        }

    }

    private void setImages() {
        bassImage.setImage(new Image(getClass().getResourceAsStream("images/kick.png")));
        snareImage.setImage(new Image(getClass().getResourceAsStream("images/snare.png")));
        hiHatImage.setImage(new Image(getClass().getResourceAsStream("images/hihat.png")));
        crashImage.setImage(new Image(getClass().getResourceAsStream("images/crash.png")));
        mTomImage.setImage(new Image(getClass().getResourceAsStream("images/tom.png")));
        fTomImage.setImage(new Image(getClass().getResourceAsStream("images/floortom.png")));
        rideImage.setImage(new Image(getClass().getResourceAsStream("images/ride.png")));
    }

    void setDrums(Drum[] drums) {
        this.drums = drums;
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
            if (!showingMixer) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mixer.fxml"));

                Scene scene = new Scene(loader.load());
                mixerStage = new Stage();

                MixerController mc = loader.getController();
                mc.setMixerValues(drums, this);

                mixerStage.setScene(scene);
                mixerStage.setTitle("Mixer");
                mixerStage.setResizable(false);

                showingMixer = true;
                mixerStage.showAndWait();
                showingMixer = false;
            } else {
                mixerStage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onDrumMachine() {
        try {
            if (!showingMachine) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("drum_machine.fxml"));

                Scene scene = new Scene(loader.load());
                machineStage = new Stage();

                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                machineStage.setScene(scene);
                machineStage.setTitle("Drum Machine");
                machineStage.setResizable(false);

                showingMachine = true;
                machineStage.showAndWait();
                showingMachine = false;
            } else {
                machineStage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHelp() {
        try {
            if (!showingHelp) {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("help.fxml")));
                helpStage = new Stage();

                helpStage.setScene(scene);
                helpStage.setTitle("Help");
                helpStage.setResizable(false);

                showingHelp = true;
                helpStage.showAndWait();
                showingHelp = false;
            } else {
                helpStage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGitHub() {
        services.showDocument(gitHubLink);
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
