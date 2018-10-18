package net.davidtanzer.babysteps;

public class SystemTimer implements Timer {
    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }
}
