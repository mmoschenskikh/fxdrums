module ru.spbstu.fxdrums {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.spbstu.fxdrums to javafx.fxml;
    exports ru.spbstu.fxdrums;
    exports ru.spbstu.fxdrums.controller;
}