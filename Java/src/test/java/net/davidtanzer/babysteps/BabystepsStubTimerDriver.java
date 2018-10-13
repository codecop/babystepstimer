package net.davidtanzer.babysteps;

public class BabystepsStubTimerDriver extends BabystepsTimerDriver {

    private long nextTime = System.currentTimeMillis();

    public void show() throws InterruptedException {
        super.show();
        BabystepsTimer.timer = this::getTime;
    }

    private long getTime() {
        return nextTime;
    }

    protected void resetSingleton() {
        super.resetSingleton();
        BabystepsTimer.timer = new SystemTimer();
    }

    public void waitFor(int seconds) {
        nextTime = nextTime + seconds * 1000;
        waitForRender();
    }

}
