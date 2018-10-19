package net.davidtanzer.babysteps;

/**
 * Adapter of domain concept signal to audio signal.
 */
public class AudioSignal implements BabystepsSignal {

    private final AudioClip audioClip;

    public AudioSignal(final AudioClip audioClip) {
        this.audioClip = audioClip;
    }

    @Override
    public void warning() {
        audioClip.play("2166__suburban-grilla__bowl-struck.wav");
    }

    @Override
    public void failure() {
        audioClip.play("32304__acclivity__shipsbell.wav");
    }

}
