package ru.spbstu.fxdrums.model.main;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * Player that synthesizes drums sounds using MIDI.
 * The sounds might differ on different sound cards, but no extra files needed.
 */
public class MidiPlayer implements Player {

    private final int noteNumber;
    private MidiChannel[] channels;

    public MidiPlayer(int noteNumber) {
        this.noteNumber = noteNumber;
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channels = synthesizer.getChannels();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playSound(int volume) {
        channels[9].noteOn(noteNumber, volume);
        channels[9].noteOff(noteNumber);
    }

    @Override
    public int getType() {
        return PLAYER_TYPE_MIDI;
    }
}
