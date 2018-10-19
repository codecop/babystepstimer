package net.davidtanzer.babysteps;

/**
 * Value of sub domain time to abstract milliseconds.
 */
public class ElapsedSeconds {

    final long millis; // TODO hide

    public ElapsedSeconds(long millis) {
        this.millis = millis;
    }

    public boolean isMoreOrEqual(long seconds) {
        return isMoreOrEqual(seconds, 0);
    }

    public boolean isMoreOrEqual(long seconds, int millis) {
        return this.millis >= seconds * 1000 + millis;
    }

    public boolean isBetween(long fromSeconds, long toSeconds) {
        return millis >= fromSeconds * 1000 && millis < toSeconds * 1000;
    }
}
