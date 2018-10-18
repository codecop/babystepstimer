package net.davidtanzer.babysteps;

/**
 * UI in terms of core domain.
 */
public interface UI {

    void create();

    void setActions(BabystepsActions actions);

    void display();

    void onTop();

    void notOnTop();

    void showNormal();

    void showFailure();

    void showPassed();

    boolean isNormal();

    void showTime(String timerText, boolean running);

    void destroy();

}
