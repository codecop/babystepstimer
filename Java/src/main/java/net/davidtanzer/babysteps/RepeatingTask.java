package net.davidtanzer.babysteps;

/**
 * Role for running as repeated task in technical sub domain scheduling.
 */
public interface RepeatingTask {

    boolean isRunning();

    void tick();
}
