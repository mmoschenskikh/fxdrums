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
import ru.spbstu.fxdrums.DrumsApp;
import ru.spbstu.fxdrums.model.Drum;
import ru.spbstu.fxdrums.model.DrumMachinePattern;
import ru.spbstu.fxdrums.model.PatternManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrumMachineController implements Initializable {

    public final static int TRACKS_COUNT = 7;
    public final static int LOOP_MAX_SIZE = 24;
    public final static int TEMPO_MIN_VALUE = 40;
    public final static String ID_BEAT_SELECTED = "beat-selected";
    public final static String ID_BEAT_NOT_SELECTED = "beat-not-selected";
    public final static String ID_BEAT_SELECTED_PLAYING = "beat-selected-playing";
    public final static String ID_BEAT_NOT_SELECTED_PLAYING = "beat-not-selected-playing";

    private final static int DEFAULT_BPM = 80;
    private final static Integer DEFAULT_BEATS = 8;
    @FXML
    public Button playButton;
    @FXML
    public Button stopButton;
    @FXML
    public HBox bassHBox;
    @FXML
    public HBox snareHBox;
    @FXML
    public HBox hiHatHBox;
    @FXML
    public HBox crashHBox;
    @FXML
    public HBox rideHBox;
    @FXML
    public HBox mTomHBox;
    @FXML
    public HBox fTomHBox;
    private int bpm;
    @FXML
    public ChoiceBox<Integer> beatsChoiceBox;
    @FXML
    public Label tempoLabel;
    @FXML
    public Slider tempoSlider;
    private int loopSize;
    private boolean isPlaying;
    private boolean stateSaved;
    private Drum[] drums;
    private Button[][] tracks;
    private ImageView playImageView;
    private ImageView stopImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bpm = (int) tempoSlider.getValue();
        tracks = new Button[TRACKS_COUNT][LOOP_MAX_SIZE];
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

    public void onStart() {
        startPlaying();
    }

    public void onStop() {
        stopPlaying();
    }

    public void onSave() {
        stopPlaying();
        Stage stage = (Stage) tempoLabel.getScene().getWindow();
        File file = prepareFileChooser().showSaveDialog(stage);
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
        Stage stage = (Stage) tempoLabel.getScene().getWindow();
        File file = prepareFileChooser().showOpenDialog(stage);
        if (file != null) {
            if (!stateSaved) {
                showSaveConfirmBox(() -> loadPattern(file), this::onSave);
            } else {
                loadPattern(file);
            }
        }
    }

    public void onClear() {
        stopPlaying();
        if (stateSaved) {
            clearAll();
        } else {
            showSaveConfirmBox(this::clearAll, this::onSave);
        }
    }

    public void showSaveConfirmBox(Action okAction, Action saveAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmbox.fxml"));
            Scene scene = new Scene(loader.load());
            Stage confirmStage = new Stage();

            confirmStage.setScene(scene);
            confirmStage.setTitle("Unsaved progress");
            confirmStage.getIcons().add(DrumsApp.icon);
            confirmStage.setResizable(false);
            confirmStage.initModality(Modality.APPLICATION_MODAL);

            ConfirmBoxController cb = loader.getController();
            cb.setMessage("All unsaved changes will be lost.");
            cb.setOkAction(okAction);
            cb.setSaveAction(saveAction);

            confirmStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDrums(Drum[] drums) {
        this.drums = drums;
    }

    public boolean isStateSaved() {
        return stateSaved;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * Generates buttons for pattern representation.
     */
    private void createAllTracks() { // Cannot use array or list here because then HBoxes are considered as nulls
        tracks[0] = createTrack(bassHBox);
        tracks[1] = createTrack(snareHBox);
        tracks[2] = createTrack(hiHatHBox);
        tracks[3] = createTrack(crashHBox);
        tracks[4] = createTrack(rideHBox);
        tracks[5] = createTrack(mTomHBox);
        tracks[6] = createTrack(fTomHBox);
    }

    /**
     * Generates buttons for single track.
     *
     * @param hBox hBox of a track.
     * @return array of Button.
     */
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

    /**
     * Prepares icons for Play and Stop buttons.
     */
    private void setImageViews() {
        playImageView = new ImageView(new Image(getClass().getResourceAsStream("images/play.png")));
        playImageView.setFitHeight(15);
        playImageView.setFitWidth(15);

        stopImageView = new ImageView(new Image(getClass().getResourceAsStream("images/stop.png")));
        stopImageView.setFitHeight(15);
        stopImageView.setFitWidth(15);
    }

    /**
     * Sets number of beats available.
     */
    private void setBeatsNumber(int number) {
        for (Button[] track : tracks) {
            for (int i = 0; i < 24; i++) {
                track[i].setDisable(i >= number);
            }
        }
    }

    private void startPlaying() {
        isPlaying = true;
        beatsChoiceBox.setDisable(true);
        invertPlayStopState();
        new Thread(() -> {
            boolean stoppedProperly = false;
            while (isPlaying) {
                for (int i = 0; i < loopSize; i++) {
                    extinguishButtons(i == 0 ? loopSize - 1 : i - 1);
                    if (!isPlaying) {
                        invertPlayStopState();
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
                invertPlayStopState();
            }
        }).start();
    }

    private void invertPlayStopState() {
        playButton.setDisable(!playButton.isDisabled());
        stopButton.setDisable(!stopButton.isDisabled());
    }

    private void extinguishButtons(int beat) {
        for (Button[] track : tracks) {
            Button button = track[beat];
            if (button.getId().startsWith(ID_BEAT_SELECTED)) {
                button.setId(ID_BEAT_SELECTED);
            } else {
                button.setId(ID_BEAT_NOT_SELECTED);
            }
        }
    }

    private void stopPlaying() {
        isPlaying = false;
        beatsChoiceBox.setDisable(false);
    }

    private FileChooser prepareFileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select file...");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pattern files", "*.pf"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        return chooser;
    }

    private void showAlertBox(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("alertbox.fxml"));
            Scene scene = new Scene(loader.load());
            Stage alertStage = new Stage();

            alertStage.setScene(scene);
            alertStage.setTitle(title);
            alertStage.getIcons().add(DrumsApp.icon);
            alertStage.setResizable(false);
            alertStage.initModality(Modality.APPLICATION_MODAL);

            AlertBoxController ab = loader.getController();
            ab.setMessage(message);

            alertStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPattern(File file) {
        try {
            DrumMachinePattern pattern = PatternManager.load(file);
            clearAll();
            applyPattern(pattern);
            stateSaved = true;
        } catch (IOException e) {
            showAlertBox("Cannot load file", e.getMessage());
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
}