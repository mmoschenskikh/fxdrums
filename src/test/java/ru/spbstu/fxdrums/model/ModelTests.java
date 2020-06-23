package ru.spbstu.fxdrums.model;

import org.junit.jupiter.api.Test;
import ru.spbstu.fxdrums.model.main.Drum;
import ru.spbstu.fxdrums.model.main.FromFilePlayer;
import ru.spbstu.fxdrums.model.main.MidiPlayer;

import java.io.FileNotFoundException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.spbstu.fxdrums.model.main.Player.PLAYER_TYPE_FILE;
import static ru.spbstu.fxdrums.model.main.Player.PLAYER_TYPE_MIDI;


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
        final int volume = new Random().nextInt(100);
        snare.setVolume(volume);

        assertEquals(volume, snare.getVolume());
        assertEquals(PLAYER_TYPE_MIDI, snare.getPlayerType());

        snare.setPlayerType(PLAYER_TYPE_FILE);

        assertEquals(volume, snare.getVolume());
        assertEquals(PLAYER_TYPE_FILE, snare.getPlayerType());
    }

}
