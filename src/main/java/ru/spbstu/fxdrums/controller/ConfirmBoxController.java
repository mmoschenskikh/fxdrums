package ru.spbstu.fxdrums.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

interface Action {
    void act();
}

public class ConfirmBoxController {

    @FXML
    public Label message;
    private Action okAction;
    private Action saveAction;

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public ConfirmBoxController setOkAction(Action action) {
        okAction = action;
        return this;
    }

    public ConfirmBoxController setSaveAction(Action action) {
        saveAction = action;
        return this;
    }

    private void closeConfirmBox() {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.close();
    }

    public void onOk() {
        closeConfirmBox();
        okAction.act();
    }

    public void onSave() {
        closeConfirmBox();
        saveAction.act();
    }

    public void onCancel() {
        closeConfirmBox();
    }
}