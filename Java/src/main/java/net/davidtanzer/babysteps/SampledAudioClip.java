package net.davidtanzer.babysteps;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Implementation of (sub domain) audio technology.
 */
public class SampledAudioClip implements AudioClip {

    @Override
    public void play(String url) {
        try (Clip clip = AudioSystem.getClip();
                InputStream audioResource = BabystepsTimer.class.getResourceAsStream("/" + url);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioResource)) {
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
