package net.davidtanzer.babysteps;

public class BabystepsStubTimerDriver extends BabystepsTimerDriver {

    private long nextTime = System.currentTimeMillis();
    private String lastAudioClip;

    @Override
    public void show() {
        super.show();
        sut.timer = this::getTime;
        sut.audioClip = this::playAudioClip;
    }

    private long getTime() {
        return nextTime;
    }

    private synchronized void playAudioClip(String name) {
        lastAudioClip = name;
    }

    @Override
    protected void resetSingleton() {
        super.resetSingleton();
        sut.timer = new SystemTimer();
        nextTime = System.currentTimeMillis();
        sut.audioClip = new SampledAudioClip();
        lastAudioClip = null;
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
