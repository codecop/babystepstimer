package net.davidtanzer.babysteps;

public class BabystepsStubTimerDriver extends BabystepsTimerDriver {

    private long nextTime;
    private String lastAudioClip;

    @Override
    protected BabystepsTimer createTimer() {
        nextTime = System.currentTimeMillis();
        lastAudioClip = null;
        return new BabystepsTimer(new SystemClock(this::getTime), new AudioSignal(this::playAudioClip));
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
