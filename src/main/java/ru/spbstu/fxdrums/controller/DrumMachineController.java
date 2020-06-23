package ru.spbstu.fxdrums.controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DrumMachineController implements Initializable {

    @FXML
    public Button playStop;
    @FXML
    public ChoiceBox<Integer> beatsChoiceBox;
    @FXML
    public Label tempoLabel;
    @FXML
    public Slider tempoSlider;
    @FXML
    public HBox bassHBox, snareHBox, hiHatHBox, crashHBox, rideHBox, mTomHBox, fTomHBox;
    private Button[][] tracks;
    private ImageView playImageView, stopImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tracks = new Button[7][24];
        createAllTracks();

        setImageViews();
        stopPlaying();
        playStop.setOnAction(event -> {
            if (playStop.getText().equals("Stop")) {
                stopPlaying();
            } else {
                startPlaying();
            }
        });

        beatsChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) ->
                setBeatsNumber(beatsChoiceBox.getValue()));
        beatsChoiceBox.getSelectionModel().select(Integer.valueOf(8));
        tempoLabel.textProperty().bind(Bindings.format("Tempo: %.0f BPM", tempoSlider.valueProperty()));
    }

    private void startPlaying() {
        playStop.setGraphic(stopImageView);
        playStop.setText("Stop");
    }

    private void stopPlaying() {
        playStop.setGraphic(playImageView);
        playStop.setText("Play");
    }

    private void setImageViews() {
        playImageView = new ImageView(new Image(getClass().getResourceAsStream("images/play.png")));
        playImageView.setFitHeight(12);
        playImageView.setFitWidth(12);

        stopImageView = new ImageView(new Image(getClass().getResourceAsStream("images/stop.png")));
        stopImageView.setFitHeight(12);
        stopImageView.setFitWidth(12);
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
            button.setOnAction(event -> {
                String cssId = button.getId();
                if (cssId == null || cssId.equals("beat-not-selected")) {
                    button.setId("beat-selected");
                } else {
                    button.setId("beat-not-selected");
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

    public void onBeatsChoice(ActionEvent event) {
    }

}

