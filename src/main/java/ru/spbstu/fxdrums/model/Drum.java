package ru.spbstu.fxdrums.model;


import java.io.FileNotFoundException;

import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_FILE;
import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_MIDI;

public enum Drum {
    BASS(36),
    SNARE(38),
    HI_HAT(42),
    CRASH(55),
    RIDE(51),
    MEDIUM_TOM(47),
    FLOOR_TOM(41);

    private final int noteNumber;
    private int volume;
    private Player player;

    Drum(int noteNumber) {
        this.noteNumber = noteNumber;
        player = new MidiPlayer(noteNumber);
        this.volume = 100;
    }

    public int getPlayerType() {
        return player.getType();
    }

    public void setPlayer(int type) throws FileNotFoundException {
        switch (type) {
            case PLAYER_TYPE_MIDI:
                player = new MidiPlayer(noteNumber);
                break;
            case PLAYER_TYPE_FILE:
                player = new FromFilePlayer(noteNumber);
                break;
        }
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void makeSound() {
        player.playSound(volume);
    }
}


