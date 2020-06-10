package ru.spbstu.fxdrums.model;

public interface Player {

    int PLAYER_TYPE_MIDI = 0;
    int PLAYER_TYPE_FILE = 1;

    void playSound(int volume);

    int getType();
}
