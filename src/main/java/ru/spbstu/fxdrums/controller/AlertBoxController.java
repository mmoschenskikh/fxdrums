package ru.spbstu.fxdrums.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertBoxController {
    @FXML
    public Label message;

    public void onOk() {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.close();
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }
}
