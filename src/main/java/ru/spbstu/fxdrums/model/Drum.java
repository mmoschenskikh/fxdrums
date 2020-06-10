package ru.spbstu.fxdrums.model;

public enum Drum {
    BASS(36),
    SNARE(38),
    HI_HAT(42),
    CRASH(55),
    RIDE(51),
    HIGH_TOM(50),
    MEDIUM_TOM(47),
    FLOOR_TOM(41);

    private final MidiPlayer player;
    private int volume;

    Drum(int noteNumber) {
        player = new MidiPlayer(noteNumber);
        this.volume = 100;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void makeSound() {
        player.playSound(volume);
    }
}


