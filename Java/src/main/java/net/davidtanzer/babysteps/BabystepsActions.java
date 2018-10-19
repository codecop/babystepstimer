package net.davidtanzer.babysteps;

/**
 * (Domain) actions of the timer from the UI. These are the commands it can respond to.
 */
public interface BabystepsActions {

    void start();

    void stop();

    void reset();

    void quit();

}
