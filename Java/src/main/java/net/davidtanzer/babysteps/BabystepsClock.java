package net.davidtanzer.babysteps;

/**
 * Time (sub domain) in terms of the domain.
 */
public interface BabystepsClock {

    void resetCycle();

    ElapsedSeconds getElapsedTime();
}
