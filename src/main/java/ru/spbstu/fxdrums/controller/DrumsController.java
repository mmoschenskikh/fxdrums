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
import ru.spbstu.fxdrums.DrumsApp;
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
    public RadioMenuItem typeMidi;

    private HostServices services;
    private String gitHubLink;
    // Boolean values to prevent opening more than one instance of window.
    private boolean showingHelp = false;
    private boolean showingMixer = false;
    private boolean showingMachine = false;
    @FXML
    public RadioMenuItem typeFile;
    @FXML
    public ImageView bassImage;
    @FXML
    public ImageView snareImage;

    @FXML
    public ToggleGroup soundType;
    @FXML
    public ImageView hiHatImage;
    @FXML
    public ImageView crashImage;
    @FXML
    public ImageView mTomImage;
    @FXML
    public ImageView fTomImage;
    @FXML
    public ImageView rideImage;
    private Drum[] drums = {bass, snare, hiHat, crash, ride, mTom, fTom};
    // Stage objects to change focus if the window is already opened.
    private Stage mixerStage;
    private Stage helpStage;
    private Stage machineStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImages();
        try {
            PropertyResourceBundle bundle = new PropertyResourceBundle(getClass().getResourceAsStream("strings/drums_strings"));
            typeMidi.setText(bundle.getString("midi_toggle_label"));
            typeFile.setText(bundle.getString("file_toggle_label"));
            gitHubLink = bundle.getString("github_link");
        } catch (IOException e) {
            e.printStackTrace();
        }
        soundType.selectedToggleProperty().addListener((obs, ov, nv) -> {
            if (typeMidi.isSelected())
                setPlayerType(PLAYER_TYPE_MIDI);
            else
                setPlayerType(PLAYER_TYPE_FILE);
        });
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
                mixerStage.getIcons().add(DrumsApp.icon);
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

                DrumMachineController dmc = loader.getController();
                dmc.setDrums(drums);

                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                machineStage.setScene(scene);
                machineStage.setTitle("Drum Machine");
                machineStage.getIcons().add(DrumsApp.icon);
                machineStage.setResizable(false);

                machineStage.setOnCloseRequest(event -> {
                    dmc.setPlaying(false);
                    if (!dmc.isStateSaved()) {
                        dmc.showSaveConfirmBox(machineStage::close, dmc::onSave);
                        event.consume();
                    }
                });

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
                helpStage.getIcons().add(DrumsApp.icon);
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
        System.exit(0);
    }

    public void onBass() {
        makeSound(bass);
    }

    public void onSnare() {
        makeSound(snare);
    }

    public void onHiHat() {
        makeSound(hiHat);
    }

    public void onCrash() {
        makeSound(crash);
    }

    public void onMTom() {
        makeSound(mTom);
    }

    public void onRide() {
        makeSound(ride);
    }

    public void onFTom() {
        makeSound(fTom);
    }

    public void setHostServices(HostServices services) {
        this.services = services;
    }

    void setDrums(Drum[] drums) {
        this.drums = drums;
    }

    /**
     * Set images for every single drum.
     */
    private void setImages() {
        bassImage.setImage(new Image(getClass().getResourceAsStream("images/kick.png")));
        snareImage.setImage(new Image(getClass().getResourceAsStream("images/snare.png")));
        hiHatImage.setImage(new Image(getClass().getResourceAsStream("images/hihat.png")));
        crashImage.setImage(new Image(getClass().getResourceAsStream("images/crash.png")));
        mTomImage.setImage(new Image(getClass().getResourceAsStream("images/tom.png")));
        fTomImage.setImage(new Image(getClass().getResourceAsStream("images/floortom.png")));
        rideImage.setImage(new Image(getClass().getResourceAsStream("images/ride.png")));
    }

    /**
     * Changes the player type for every single drum.
     * If PLAYER_TYPE_FILE is chosen and some of the files were not found, PLAYER_TYPE_MIDI will be set.
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

    private void makeSound(Drum drum) {
        if (!showingMachine) drum.makeSound();
    }
}
