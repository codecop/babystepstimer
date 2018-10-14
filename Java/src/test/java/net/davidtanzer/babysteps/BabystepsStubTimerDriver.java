package net.davidtanzer.babysteps;

import org.junit.Assert;

public class BabystepsStubTimerDriver extends BabystepsTimerDriver {

    private long nextTime = System.currentTimeMillis();
    private String lastAudioClip;

    public void show() throws InterruptedException {
        super.show();
        BabystepsTimer.timer = this::getTime;
        BabystepsTimer.audioClip = this::playAudioClip;
    }

    private long getTime() {
        return nextTime;
    }

    private void playAudioClip(String name) {
        lastAudioClip = name;
    }

    protected void resetSingleton() {
        super.resetSingleton();
        BabystepsTimer.timer = new SystemTimer();
        nextTime = System.currentTimeMillis();
        BabystepsTimer.audioClip = new SampledAudioClip();
        lastAudioClip = null;
    }

    public void waitFor(int seconds) {
        nextTime = nextTime + seconds * 1000;
        waitForRender();
    }

    public void assertAudioClipPlayed(String expectedName) {
        Assert.assertEquals(expectedName, lastAudioClip);
        lastAudioClip = null;
    }
}
