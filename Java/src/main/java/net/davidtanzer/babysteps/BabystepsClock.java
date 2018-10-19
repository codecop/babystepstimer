package net.davidtanzer.babysteps;

/**
 * Time in terms of the core domain.
 */
public interface BabystepsClock {

    void resetCycle();

    ElapsedSeconds getElapsedTime();
}
