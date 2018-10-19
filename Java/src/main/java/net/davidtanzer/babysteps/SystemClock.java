package net.davidtanzer.babysteps;

/**
 * Implementation of sub domain concept time.
 */
public class SystemClock {

    private final Timer timer;
    private long currentCycleStartTime;

    public SystemClock(Timer timer) {
        this.timer = timer;
    }

    public void reset() {
        currentCycleStartTime = timer.getTime();
    }

    public long getElapsedTime() {
        return timer.getTime() - currentCycleStartTime;
    }
}
