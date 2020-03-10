package net.davidtanzer.babysteps;

public class TimerModel {

    String bodyBackgroundColor;
    boolean timerRunning;
    long currentCycleStartTime;
    String lastRemainingTime;

    public TimerModel(String bodyBackgroundColor) {
        this.bodyBackgroundColor = bodyBackgroundColor;
    }

    
}
