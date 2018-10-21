package net.davidtanzer.babysteps;

/**
 * Implementation of getting the current time.
 */
public class SystemTimer implements Timer {

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }

}
