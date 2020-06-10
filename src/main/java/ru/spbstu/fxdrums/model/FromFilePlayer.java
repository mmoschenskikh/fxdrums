package ru.spbstu.fxdrums.model;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaException;

import java.io.File;
import java.io.FileNotFoundException;

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
        clip.play(volume);
    }

    @Override
    public int getType() {
        return PLAYER_TYPE_FILE;
    }
}
