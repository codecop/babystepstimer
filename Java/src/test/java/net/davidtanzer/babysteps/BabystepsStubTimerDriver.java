package net.davidtanzer.babysteps;

public class BabystepsStubTimerDriver extends BabystepsTimerDriver {

    private long nextTime;
    private String lastAudioClip;

    @Override
    protected BabystepsTimer createTimer() {
        nextTime = System.currentTimeMillis();
        lastAudioClip = null;
        return new BabystepsTimer(new SystemClock(this::getTime),
                                  new AudioSignal(this::playAudioClip),
                                  new SwingUI(),
                                  new RepeatingTaskThreadScheduler());
    }

    private long getTime() {
        return nextTime;
    }

    private synchronized void playAudioClip(final String name) {
        lastAudioClip = name;
    }

    @Override
    public void waitFor(final int seconds) {
        nextTime = nextTime + seconds * 1000;
        waitForTimerThread();
    }

    public synchronized void assertAudioClipPlayed(final String expectedName) {
        RetryAssert.assertEquals(expectedName, () -> lastAudioClip, 100);
        lastAudioClip = null;
    }
}
