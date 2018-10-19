package net.davidtanzer.babysteps;

/**
 * Role for running as repeated task.
 */
public interface RepeatingTask {

    boolean isRunning();

    void tick();
}
