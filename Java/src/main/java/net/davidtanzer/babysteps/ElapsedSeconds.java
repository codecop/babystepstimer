package net.davidtanzer.babysteps;

/**
 * Value object of sub domain time to abstract milliseconds.
 */
public class ElapsedSeconds {

    public static final ElapsedSeconds NONE = fromMillis(0L);
    private final long millis;

    public static ElapsedSeconds fromMillis(long millis) {
        return new ElapsedSeconds(millis);
    }

    private ElapsedSeconds(long millis) {
        this.millis = millis;
    }

    public boolean areMoreOrEqual(long seconds) {
        return areMoreOrEqual(seconds, 0);
    }

    public boolean areMoreOrEqual(long seconds, int millis) {
        return this.millis >= seconds * 1000 + millis;
    }

    public boolean areBetween(long fromSeconds, long toSeconds) {
        return millis >= fromSeconds * 1000 && millis < toSeconds * 1000;
    }

    public long get() {
        return millis / 1000;
    }

}
