package net.davidtanzer.babysteps;

/**
 * Implementation of sub domain concept time.
 */
public class SystemClock implements BabystepsClock {

    private final Timer timer;
    private long currentCycleStartTime;

    public SystemClock(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void resetCycle() {
        currentCycleStartTime = timer.getTime();
    }

    @Override
    public ElapsedSeconds getElapsedTime() {
        return new ElapsedSeconds(timer.getTime() - currentCycleStartTime);
    }
}
