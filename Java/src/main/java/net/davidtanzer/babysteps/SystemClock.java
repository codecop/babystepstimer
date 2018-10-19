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

    public void resetCycle() {
        currentCycleStartTime = timer.getTime();
    }

    public ElapsedSeconds getElapsedTime() {
        return new ElapsedSeconds(timer.getTime() - currentCycleStartTime);
    }
}
