package ru.spbstu.fxdrums.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.spbstu.fxdrums.model.Drum;
import ru.spbstu.fxdrums.model.DrumMachinePattern;
import ru.spbstu.fxdrums.model.PatternManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrumMachineController implements Initializable {

    private final static String ID_BEAT_SELECTED = "beat-selected";
    private final static String ID_BEAT_NOT_SELECTED = "beat-not-selected";
    private final static String ID_BEAT_SELECTED_PLAYING = "beat-selected-playing";
    private final static String ID_BEAT_NOT_SELECTED_PLAYING = "beat-not-selected-playing";

    private final static int DEFAULT_BPM = 80;
    private final static Integer DEFAULT_BEATS = 8;

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
    private int bpm;
    private boolean isPlaying;
    private boolean stateSaved;
    private ImageView playImageView, stopImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tracks = new Button[7][24];
        bpm = (int) tempoSlider.getValue();
        createAllTracks();

        setImageViews();
        playButton.setGraphic(playImageView);
        stopButton.setGraphic(stopImageView);
        stopButton.setDisable(true);

        beatsChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            stateSaved = false;
            loopSize = beatsChoiceBox.getValue();
            setBeatsNumber(loopSize);
        });
        beatsChoiceBox.getSelectionModel().select(DEFAULT_BEATS);
        tempoLabel.textProperty().bind(Bindings.format("Tempo: %.0f BPM", tempoSlider.valueProperty()));
        tempoSlider.valueProperty().addListener((obs, ov, nv) -> {
            stateSaved = false;
            bpm = (int) tempoSlider.getValue();
        });
        stateSaved = true;
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
                        if (prevButton.getId().startsWith(ID_BEAT_SELECTED)) {
                            prevButton.setId(ID_BEAT_SELECTED);
                        } else {
                            prevButton.setId(ID_BEAT_NOT_SELECTED);
                        }
                    }
                    if (!isPlaying) {
                        changeButtonStates();
                        stoppedProperly = true;
                        break;
                    }
                    for (int j = 0; j < tracks.length; j++) {
                        Button button = tracks[j][i];
                        if (button.getId().equals(ID_BEAT_SELECTED)) {
                            drums[j].makeSound();
                            button.setId(ID_BEAT_SELECTED_PLAYING);
                        } else {
                            button.setId(ID_BEAT_NOT_SELECTED_PLAYING);
                        }
                    }
                    try {
                        Thread.sleep(60 * 1000 / bpm);
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

    public void onSave() {
        stopPlaying();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save pattern...");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pattern files", "*.pf"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        Stage stage = (Stage) tempoLabel.getScene().getWindow();
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            try {
                PatternManager.save(new DrumMachinePattern(tracks, loopSize, bpm), file);
                stateSaved = true;
            } catch (IOException e) {
                showAlertBox("Cannot save file", e.getMessage());
            }
        }
    }

    public void onLoad() {
        stopPlaying();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load pattern...");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pattern files", "*.pf"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        Stage stage = (Stage) tempoLabel.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            if (!stateSaved) {
                showSaveConfirmBox(() -> loadPattern(file), this::onSave);
            } else {
                loadPattern(file);
            }

        }
    }

    private void loadPattern(File file) {
        try {
            DrumMachinePattern pattern = PatternManager.read(file);
            clearAll();
            applyPattern(pattern);
        } catch (IOException e) {
            showAlertBox("Cannot load file", e.getMessage());
        }
    }

    public void onClear() {
        if (stateSaved) {
            clearAll();
        } else {
            showSaveConfirmBox(this::clearAll, this::onSave);
        }
    }

    private void applyPattern(DrumMachinePattern pattern) {
        beatsChoiceBox.getSelectionModel().select(Integer.valueOf(pattern.getLoopSize()));
        tempoSlider.setValue(pattern.getTempo());
        boolean[][] importedTracks = pattern.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            for (int j = 0; j < tracks[i].length; j++) {
                if (importedTracks[i][j]) {
                    tracks[i][j].setId(ID_BEAT_SELECTED);
                }
            }
        }
    }

    private void clearAll() {
        for (Button[] track : tracks) {
            for (Button button : track) {
                button.setId(ID_BEAT_NOT_SELECTED);
            }
        }
        tempoSlider.setValue(DEFAULT_BPM);
        beatsChoiceBox.getSelectionModel().select(DEFAULT_BEATS);
        stateSaved = true;
    }

    private void showSaveConfirmBox(Action okAction, Action saveAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmbox.fxml"));
            Scene scene = new Scene(loader.load());
            Stage confirmStage = new Stage();

            confirmStage.setScene(scene);
            confirmStage.setTitle("Unsaved progress");
            confirmStage.setResizable(false);
            confirmStage.initModality(Modality.APPLICATION_MODAL);

            ConfirmBoxController cb = loader.getController();
            cb.setMessage("All unsaved changes will be lost");
            cb.setOkAction(okAction);
            cb.setSaveAction(saveAction);

            confirmStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlertBox(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("alertbox.fxml"));
            Scene scene = new Scene(loader.load());
            Stage alertStage = new Stage();

            alertStage.setScene(scene);
            alertStage.setTitle(title);
            alertStage.setResizable(false);
            alertStage.initModality(Modality.APPLICATION_MODAL);

            AlertBoxController ab = loader.getController();
            ab.setMessage(message);

            alertStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDrums(Drum[] drums) {
        this.drums = drums;
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
            button.setId(ID_BEAT_NOT_SELECTED);
            button.setOnAction(event -> {
                stateSaved = false;
                switch (button.getId()) {
                    case ID_BEAT_NOT_SELECTED:
                        button.setId(ID_BEAT_SELECTED);
                        break;
                    case ID_BEAT_NOT_SELECTED_PLAYING:
                        button.setId(ID_BEAT_SELECTED_PLAYING);
                        break;
                    case ID_BEAT_SELECTED:
                        button.setId(ID_BEAT_NOT_SELECTED);
                        break;
                    case ID_BEAT_SELECTED_PLAYING:
                        button.setId(ID_BEAT_NOT_SELECTED_PLAYING);
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