package ru.spbstu.fxdrums.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    @FXML
    public Label overviewHelp, controlsHelp, soundHelp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PropertyResourceBundle bundle = new PropertyResourceBundle(getClass().getResourceAsStream("strings/help_strings"));
            overviewHelp.setText(bundle.getString("overview"));
            controlsHelp.setText(bundle.getString("controls"));
            soundHelp.setText(bundle.getString("sound_types"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
