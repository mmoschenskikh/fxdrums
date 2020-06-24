package ru.spbstu.fxdrums.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static ru.spbstu.fxdrums.controller.DrumMachineController.*;

/**
 * Saver and loader for DrumMachinePattern.
 */
public class PatternManager {

    // Minimum tempo is represented as -128 to fit all potential values in one byte
    private final static int TEMPO_OFFSET = Byte.MIN_VALUE - TEMPO_MIN_VALUE;

    public static void save(DrumMachinePattern pattern, File outputFile) throws IOException {
        byte[] bytes = new byte[LOOP_MAX_SIZE + 1];
        int loopSize = pattern.getLoopSize();

        bytes[0] = (byte) (pattern.getTempo() + TEMPO_OFFSET); // Tempo
        for (int i = 0; i < loopSize; i++) { // (loopSize) bytes of pattern
            byte beat = 0;
            for (boolean[] track : pattern.getTracks()) {
                if (track[i])
                    beat += 1;
                beat <<= 1;
            }
            bytes[1 + i] = beat;
        }
        for (int i = loopSize + 1; i < bytes.length; i++) { // (LOOP_MAX_SIZE - loopSize) bytes to represent disabled beats
            bytes[i] = 0b1111111;
        }

        Files.write(outputFile.toPath(), bytes);
    }

    public static DrumMachinePattern load(File inputFile) throws IOException {
        byte[] bytes = Files.readAllBytes(inputFile.toPath());
        boolean[][] tracks = new boolean[TRACKS_COUNT][LOOP_MAX_SIZE];
        int loopSize = LOOP_MAX_SIZE;

        int tempo = bytes[0] - TEMPO_OFFSET;
        for (int i = 1; i < bytes.length; i++) {
            byte beat = bytes[i];
            if (beat == 0b1111111) {
                loopSize = i - 1;
                break;
            } else if (beat == 0) {
                continue;
            }
            for (int j = 6; j >= 0; j--) {
                beat >>= 1;
                tracks[j][i - 1] = (beat & 1) == 1;
            }
        }

        return new DrumMachinePattern(tracks, loopSize, tempo);
    }
}
