package net.davidtanzer.babysteps;

/**
 * UI in terms of the core domain.
 */
public interface BabystepsUI {

    void create();

    void setActions(BabystepsActions actions);

    void display();

    void onTop();

    void notOnTop();

    void showNormal();

    void showFailure();

    void showPassed();

    boolean isNormal();

    void showTime(String timeCaption, boolean running);

    void destroy();

}
