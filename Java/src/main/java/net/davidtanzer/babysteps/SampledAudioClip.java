package net.davidtanzer.babysteps;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Implementation of (sub domain) audio technology.
 */
public class SampledAudioClip implements AudioClip {

    @Override
    public synchronized void play(final String url) {
        new Thread(() -> playSound(url)).start();
    }

    private void playSound(final String url) {
        try (Clip clip = AudioSystem.getClip();
             InputStream audioResource = BabystepsTimer.class.getResourceAsStream('/' + url);
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioResource)) {
            clip.open(audioStream);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println(e.getMessage());
        }
    }
}
