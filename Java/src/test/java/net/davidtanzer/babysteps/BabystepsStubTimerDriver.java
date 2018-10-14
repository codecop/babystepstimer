package net.davidtanzer.babysteps;

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

    private synchronized void playAudioClip(String name) {
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
        waitForTimerThread();
    }

    public synchronized void assertAudioClipPlayed(String expectedName) {
        RetryAssert.assertEquals(expectedName, () -> lastAudioClip, 100);
        lastAudioClip = null;
    }
}
