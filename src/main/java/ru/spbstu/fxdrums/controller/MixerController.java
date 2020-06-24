package ru.spbstu.fxdrums.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import ru.spbstu.fxdrums.model.Drum;

public class MixerController {

    private Drum[] drums;

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

    public void setMixerValues(Drum[] drums) {
        this.drums = drums;
        setSliderVolumes();
        setSliderListeners();
    }

    private void setSliderListeners() {
        kickVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
        snareVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
        hiHatVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
        crashVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
        rideVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
        mTomVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
        fTomVol.valueProperty().addListener((obs, ov, nv) -> getSliderVolumes());
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
}
