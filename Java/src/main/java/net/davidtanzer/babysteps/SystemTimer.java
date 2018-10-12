package net.davidtanzer.babysteps;

public class SystemTimer implements Timer {
    public long getTime() {
        return System.currentTimeMillis();
    }
}
