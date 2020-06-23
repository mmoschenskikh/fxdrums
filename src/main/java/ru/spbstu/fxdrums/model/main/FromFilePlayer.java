package ru.spbstu.fxdrums.model.main;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Player that plays drums sounds from external files.
 * The sounds are the same on different sound cards, but extra files are needed.
 */
public class FromFilePlayer implements Player {

    private final AudioClip clip;

    public FromFilePlayer(int noteNumber) throws FileNotFoundException {
        File sound = new File("sounds" + File.separatorChar + noteNumber + ".wav");
        try {
            clip = new AudioClip(sound.toURI().toString());
        } catch (MediaException e) {
            throw new FileNotFoundException("File " + noteNumber + ".wav not found.");
        }
    }

    public void playSound(int volume) {
        clip.play(volume / 100.0);
    }

    @Override
    public int getType() {
        return PLAYER_TYPE_FILE;
    }
}
