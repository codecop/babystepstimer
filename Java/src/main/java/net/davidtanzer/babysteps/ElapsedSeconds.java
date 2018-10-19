package net.davidtanzer.babysteps;

/**
 * Value object of sub domain time to abstract milliseconds.
 */
public class ElapsedSeconds {

    public static final ElapsedSeconds NONE = new ElapsedSeconds(0L);
    private final long millis;

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

    public long getSeconds() {
        return millis / 1000;
    }

}
