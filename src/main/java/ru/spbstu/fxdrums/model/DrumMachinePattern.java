package ru.spbstu.fxdrums.model;

import javafx.scene.control.Button;

import java.util.Arrays;

import static ru.spbstu.fxdrums.controller.DrumMachineController.*;

public class DrumMachinePattern {

    private final boolean[][] tracks;
    private final int loopSize;
    private final int tempo;

    public DrumMachinePattern(boolean[][] tracks, int loopSize, int tempo) {
        this.tracks = tracks;
        this.loopSize = loopSize;
        this.tempo = tempo;
    }

    public DrumMachinePattern(Button[][] tracks, int loopSize, int tempo) {
        this.loopSize = loopSize;
        this.tempo = tempo;

        boolean[][] newTracks = new boolean[TRACKS_COUNT][LOOP_MAX_SIZE];
        for (int i = 0; i < tracks.length; i++) {
            for (int j = 0; j < tracks[i].length; j++) {
                newTracks[i][j] = tracks[i][j].getId().equals(ID_BEAT_SELECTED);
            }
        }
        this.tracks = newTracks;
    }

    public boolean[][] getTracks() {
        return tracks;
    }

    public int getLoopSize() {
        return loopSize;
    }

    public int getTempo() {
        return tempo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object instanceof DrumMachinePattern) {
            DrumMachinePattern pattern = (DrumMachinePattern) object;
            return loopSize == pattern.loopSize &&
                    tempo == pattern.tempo &&
                    Arrays.deepEquals(tracks, pattern.tracks);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(tracks);
        result = 31 * result + loopSize;
        result = 31 * result + tempo;
        return result;
    }
}
