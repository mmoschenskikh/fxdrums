module ru.spbstu.fxdrums {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens ru.spbstu.fxdrums to javafx.fxml;
    exports ru.spbstu.fxdrums;
    exports ru.spbstu.fxdrums.model;
    exports ru.spbstu.fxdrums.controller;
}