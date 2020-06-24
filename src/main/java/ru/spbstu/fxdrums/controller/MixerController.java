package ru.spbstu.fxdrums.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import ru.spbstu.fxdrums.model.Drum;

import java.net.URL;
import java.util.ResourceBundle;

public class MixerController implements Initializable {

    private Drum[] drums;
    private DrumsController dc;

    @FXML
    public Slider kickVol;
    @FXML
    public Slider snareVol;
    @FXML
    public Slider hiHatVol;
    @FXML
    public Slider crashVol;
    @FXML
    public Slider rideVol;
    @FXML
    public Slider mTomVol;
    @FXML
    public Slider fTomVol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kickVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
        snareVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
        hiHatVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
        crashVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
        rideVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
        mTomVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
        fTomVol.valueProperty().addListener((obs, ov, nv) -> setDrumVolumes());
    }

    public void setMixerValues(Drum[] drums, DrumsController dc) {
        this.drums = drums;
        this.dc = dc;
        setSliderVolumes();
    }

    private void setSliderVolumes() { // Cannot use array or list here because then sliders are considered as nulls
        kickVol.setValue(drums[0].getVolume());
        snareVol.setValue(drums[1].getVolume());
        hiHatVol.setValue(drums[2].getVolume());
        crashVol.setValue(drums[3].getVolume());
        rideVol.setValue(drums[4].getVolume());
        mTomVol.setValue(drums[5].getVolume());
        fTomVol.setValue(drums[6].getVolume());
    }

    private void getSliderVolumes() {
        drums[0].setVolume((int) kickVol.getValue());
        drums[1].setVolume((int) snareVol.getValue());
        drums[2].setVolume((int) hiHatVol.getValue());
        drums[3].setVolume((int) crashVol.getValue());
        drums[4].setVolume((int) rideVol.getValue());
        drums[5].setVolume((int) mTomVol.getValue());
        drums[6].setVolume((int) fTomVol.getValue());
    }

    /**
     * Changes volumes in DrumsController.
     */
    private void setDrumVolumes() {
        getSliderVolumes();
        dc.setDrums(drums);
    }
}
