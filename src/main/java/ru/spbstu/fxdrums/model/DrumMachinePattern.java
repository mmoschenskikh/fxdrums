package ru.spbstu.fxdrums.model;

import javafx.scene.control.Button;

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

        boolean[][] newTracks = new boolean[tracks.length][tracks[0].length];
        for (int i = 0; i < tracks.length; i++) {
            for (int j = 0; j < tracks[i].length; j++) {
                newTracks[i][j] = tracks[i][j].getId().equals("beat-selected");
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
}
