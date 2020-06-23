package ru.spbstu.fxdrums.model.machine;

public class DrumMachine {

    private final Track[] tracks = {Track.BASS_TRACK, Track.SNARE_TRACK};

    private int tempo;

    public Boolean[] getNotes(int beat) {
        Boolean[] notes = new Boolean[2];

        notes[0] = tracks[0].getNote(beat);
        notes[1] = tracks[1].getNote(beat);

        return notes;
    }

    public void setNote(int beat, String track) {
        Track t = Track.valueOf(track);
        t.addNote(beat);
    }
}
