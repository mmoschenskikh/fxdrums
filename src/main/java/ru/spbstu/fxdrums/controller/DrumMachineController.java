package ru.spbstu.fxdrums.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ru.spbstu.fxdrums.model.Drum;

import java.net.URL;
import java.util.ResourceBundle;

public class DrumMachineController implements Initializable {

    @FXML
    public Button playButton, stopButton;
    @FXML
    public ChoiceBox<Integer> beatsChoiceBox;
    @FXML
    public Label tempoLabel;
    @FXML
    public Slider tempoSlider;
    @FXML
    public HBox bassHBox, snareHBox, hiHatHBox, crashHBox, rideHBox, mTomHBox, fTomHBox;

    private Button[][] tracks;
    private Drum[] drums;
    private int loopSize;
    private double bpm;
    private boolean isPlaying;
    private ImageView playImageView, stopImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tracks = new Button[7][24];
        bpm = tempoSlider.getValue();
        createAllTracks();

        setImageViews();
        playButton.setGraphic(playImageView);
        stopButton.setGraphic(stopImageView);
        stopButton.setDisable(true);

        beatsChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            loopSize = beatsChoiceBox.getValue();
            setBeatsNumber(loopSize);
        });
        beatsChoiceBox.getSelectionModel().select(Integer.valueOf(8));
        tempoLabel.textProperty().bind(Bindings.format("Tempo: %.0f BPM", tempoSlider.valueProperty()));
        tempoSlider.valueProperty().addListener((obs, ov, nv) -> bpm = tempoSlider.getValue());
    }

    public void startPlaying() {
        isPlaying = true;
        beatsChoiceBox.setDisable(true);
        changeButtonStates();
        new Thread(() -> {
            boolean stoppedProperly = false;
            while (isPlaying) {
                for (int i = 0; i < loopSize; i++) {
                    for (Button[] track : tracks) {
                        Button prevButton = track[i == 0 ? loopSize - 1 : i - 1];
                        if (prevButton.getId().startsWith("beat-selected")) {
                            prevButton.setId("beat-selected");
                        } else {
                            prevButton.setId("beat-not-selected");
                        }
                    }
                    if (!isPlaying) {
                        changeButtonStates();
                        stoppedProperly = true;
                        break;
                    }
                    for (int j = 0; j < tracks.length; j++) {
                        Button button = tracks[j][i];
                        if (button.getId().equals("beat-selected")) {
                            drums[j].makeSound();
                            button.setId("beat-selected-playing");
                        } else {
                            button.setId("beat-not-selected-playing");
                        }
                    }
                    try {
                        Thread.sleep((long) (60 * 1000 / bpm));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!stoppedProperly) {
                extinguishButtons(loopSize - 1);
                changeButtonStates();
            }
        }).start();
    }

    public void stopPlaying() {
        beatsChoiceBox.setDisable(false);
        isPlaying = false;
    }

    private void changeButtonStates() {
        playButton.setDisable(!playButton.isDisabled());
        stopButton.setDisable(!stopButton.isDisabled());
    }

    private void extinguishButtons(int beat) {
        for (Button[] track : tracks) {
            Button button = track[beat];
            if (button.getId().startsWith("beat-selected")) {
                button.setId("beat-selected");
            } else {
                button.setId("beat-not-selected");
            }
        }
    }

    private void setImageViews() {
        playImageView = new ImageView(new Image(getClass().getResourceAsStream("images/play.png")));
        playImageView.setFitHeight(15);
        playImageView.setFitWidth(15);

        stopImageView = new ImageView(new Image(getClass().getResourceAsStream("images/stop.png")));
        stopImageView.setFitHeight(15);
        stopImageView.setFitWidth(15);
    }

    public void setDrums(Drum[] drums) {
        this.drums = drums;
    }

    private void createAllTracks() { // Cannot use array or list here because then HBoxes are considered as nulls
        tracks[0] = createTrack(bassHBox);
        tracks[1] = createTrack(snareHBox);
        tracks[2] = createTrack(hiHatHBox);
        tracks[3] = createTrack(crashHBox);
        tracks[4] = createTrack(rideHBox);
        tracks[5] = createTrack(mTomHBox);
        tracks[6] = createTrack(fTomHBox);
    }

    private Button[] createTrack(HBox hBox) {
        Button[] buttons = new Button[24];
        for (int i = 0; i < 24; i++) {
            Button button = new Button();
            button.setId("beat-not-selected");
            button.setOnAction(event -> {
                switch (button.getId()) {
                    case "beat-not-selected":
                        button.setId("beat-selected");
                        break;
                    case "beat-not-selected-playing":
                        button.setId("beat-selected-playing");
                        break;
                    case "beat-selected":
                        button.setId("beat-not-selected");
                        break;
                    case "beat-selected-playing":
                        button.setId("beat-not-selected-playing");
                        break;
                }
            });
            hBox.getChildren().add(button);
            buttons[i] = button;
        }
        return buttons;
    }

    private void setBeatsNumber(int number) {
        for (Button[] track : tracks) {
            for (int i = 0; i < 24; i++) {
                track[i].setDisable(i >= number);
            }
        }
    }
}