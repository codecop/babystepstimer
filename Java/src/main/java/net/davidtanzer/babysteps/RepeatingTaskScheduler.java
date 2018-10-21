package net.davidtanzer.babysteps;

/**
 * Abstraction over (sub domain) scheduling a repeated task.
 */
public interface RepeatingTaskScheduler {

    void schedule(RepeatingTask task, long pauseBetweenRuns);

}
