package net.davidtanzer.babysteps;

public interface TimerView {

    void registerActionListener(TimerActionListener listener);

    void showTimeRunning(String time, String bodyBackgroundColor);
    // should not use colour here, use enum for states

    void showTimeStopped(String time, String bodyBackgroundColor);
    // colour is always neutral, could drop argument

    void setBeOnTop(boolean onTop);


}
