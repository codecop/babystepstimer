package net.davidtanzer.babysteps;

/**
 * Implementation of scheduling a repeated task.
 */
public class RepeatingTaskThreadScheduler implements RepeatingTaskScheduler {

    @Override
    public void schedule(final RepeatingTask task, final long pauseBetweenRuns) {
        new RepeatingTaskThread(task, pauseBetweenRuns).start();
    }

}
