package ru.spbstu.fxdrums.model.machine;

import java.util.Set;
import java.util.TreeSet;

public enum Track {
    BASS_TRACK,
    SNARE_TRACK;

    private final Set<Integer> content = new TreeSet<>();

    public void addNote(int beat) {
        content.add(beat);
    }

    public boolean getNote(int beat) {
        return content.contains(beat);
    }

    public void deleteNote(int beat) {
        content.remove(beat);
    }
}
