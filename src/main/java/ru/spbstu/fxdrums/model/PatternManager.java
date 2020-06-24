package ru.spbstu.fxdrums.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PatternManager {

    private final static int TEMPO_OFFSET = 168; // min tempo is 40 and min byte value is -128

    public static void save(DrumMachinePattern pattern, File outputFile) throws IOException {
        byte[] bytes = new byte[25];
        bytes[0] = (byte) (pattern.getTempo() - TEMPO_OFFSET);
        int loopSize = pattern.getLoopSize();
        for (int i = 0; i < loopSize; i++) {
            byte beat = 0b0;
            for (boolean[] track : pattern.getTracks()) {
                if (track[i]) {
                    beat += 1;
                }
                beat <<= 1;
            }
            bytes[1 + i] = beat;
        }
        for (int i = loopSize + 1; i < bytes.length; i++) {
            bytes[i] = 0b1111111;
        }
        Files.write(outputFile.toPath(), bytes);
    }

    public static DrumMachinePattern read(File inputFile) throws IOException {
        byte[] bytes = Files.readAllBytes(inputFile.toPath());
        int tempo = bytes[0] + TEMPO_OFFSET;
        int loopSize = 24;
        boolean[][] tracks = new boolean[7][24];
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
