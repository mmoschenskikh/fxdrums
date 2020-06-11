package ru.spbstu.fxdrums.model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_FILE;
import static ru.spbstu.fxdrums.model.Player.PLAYER_TYPE_MIDI;


public class ModelTests {

    @Test
    public void midiPlayerTest() {
        assertEquals(PLAYER_TYPE_MIDI, new MidiPlayer(12).getType());
    }

    @Test
    public void fromFilePlayerTest() throws FileNotFoundException {
        assertThrows(FileNotFoundException.class, () -> new FromFilePlayer(1234));
        assertEquals(PLAYER_TYPE_FILE, new FromFilePlayer(42).getType());
    }

    @Test
    public void drumTest() throws FileNotFoundException {
        Drum snare = Drum.SNARE;
        snare.setVolume(45);

        assertEquals(45, snare.getVolume());
        assertEquals(PLAYER_TYPE_MIDI, snare.getPlayerType());

        snare.setPlayerType(PLAYER_TYPE_FILE);

        assertEquals(45, snare.getVolume());
        assertEquals(PLAYER_TYPE_FILE, snare.getPlayerType());
    }

}
