package net.davidtanzer.babysteps;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BabystepsTimerDriver {

    private JFrame timerFrame;
    private JTextPane timerPane;

    public void show() throws InterruptedException {
        BabystepsTimer.main(new String[0]);
        timerFrame = BabystepsTimer.timerFrame;
        timerPane = BabystepsTimer.timerPane;
        waitForRender();
    }

    private void waitForRender() {
        int timerSleepsMillisInThread = 10;
        sleep(timerSleepsMillisInThread * 2);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void assertFrameVisible() {
        assertEquals("Babysteps Timer", timerFrame.getTitle());
        assertTrue(timerFrame.isUndecorated());
        assertEquals(new Dimension(250, 120), timerFrame.getSize());
        assertTrue(timerFrame.isVisible());

        assertFalse(timerPane.isEditable());
    }

    public String getHtml() {
        return timerPane.getText();
    }

    public void clickStart(int... seconds) {
        if (seconds.length > 0) {
            BabystepsTimer.SECONDS_IN_CYCLE = seconds[0];
        }
        click("command://start");
        waitForRender();
    }

    public void clickStop() {
        BabystepsTimer.SECONDS_IN_CYCLE = 120;
        click("command://stop");
        waitForRender();
    }

    public void clickReset() {
        click("command://reset");
        waitForRender();
    }

    private void click(String s) {
        HyperlinkEvent event = new HyperlinkEvent(timerPane, HyperlinkEvent.EventType.ACTIVATED, null, s);
        timerPane.fireHyperlinkUpdate(event);
    }

    public void close() {
        stopTimer();
        resetSingleton();
    }

    private void stopTimer() {
        BabystepsTimer.timerRunning = false;
        waitForRender();
    }

    private void resetSingleton() {
        timerFrame.setVisible(false);
        timerFrame.dispose();
        BabystepsTimer.timerFrame = null;
        BabystepsTimer.timerPane = null;
        BabystepsTimer.SECONDS_IN_CYCLE = 120; // hack to speed things up
        BabystepsTimer.bodyBackgroundColor = BabystepsTimer.BACKGROUND_COLOR_NEUTRAL; // singleton fix
    }

    public void waitFor(int seconds) {
        sleep(seconds * 1000);
    }
}
