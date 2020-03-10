package net.davidtanzer.babysteps;

public interface TimerView {

    void showRunning(String time, String bodyBackgroundColor);
    void showStopped(String time, String bodyBackgroundColor);
    void setAlwaysOnTop(boolean b);

}
