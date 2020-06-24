package ru.spbstu.fxdrums.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

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
        final int volume = new Random().nextInt(100);
        snare.setVolume(volume);

        assertEquals(volume, snare.getVolume());
        assertEquals(PLAYER_TYPE_MIDI, snare.getPlayerType());

        snare.setPlayerType(PLAYER_TYPE_FILE);

        assertEquals(volume, snare.getVolume());
        assertEquals(PLAYER_TYPE_FILE, snare.getPlayerType());
    }

    @Test
    public void patternTest() throws IOException {
        DrumMachinePattern pattern = PatternManager.load(new File("pattern_samples/16beats_130bpm_pattern.pf"));
        assertEquals(16, pattern.getLoopSize());
        assertEquals(130, pattern.getTempo());

        File tempFile = new File("pattern_samples/temp.pf");
        PatternManager.save(pattern, tempFile);
        DrumMachinePattern tempPattern = PatternManager.load(tempFile);

        assertEquals(pattern, tempPattern);

        tempFile.delete();
    }
}
