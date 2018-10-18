package net.davidtanzer.babysteps;

public class BabystepsStubTimerDriver extends BabystepsTimerDriver {

    private long nextTime;
    private String lastAudioClip;

    @Override
    public void show() {
        super.show();
        sut.timer = this::getTime;
        nextTime = System.currentTimeMillis();
        sut.audioClip = this::playAudioClip;
        lastAudioClip = null;
    }

    private long getTime() {
        return nextTime;
    }

    private synchronized void playAudioClip(String name) {
        lastAudioClip = name;
    }

    @Override
    public void waitFor(int seconds) {
        nextTime = nextTime + seconds * 1000;
        waitForTimerThread();
    }

    public synchronized void assertAudioClipPlayed(String expectedName) {
        RetryAssert.assertEquals(expectedName, () -> lastAudioClip, 100);
        lastAudioClip = null;
    }
}
