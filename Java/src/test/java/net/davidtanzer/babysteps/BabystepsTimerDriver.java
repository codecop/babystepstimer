package net.davidtanzer.babysteps;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BabystepsTimerDriver {

    protected BabystepsTimer sut;
    private JFrame timerFrame;
    private JTextPane timerPane;

    public void show() {
        sut = new BabystepsTimer();
        timerFrame = sut.timerFrame;
        timerPane = sut.timerPane;
        waitForRender();
    }

    private void waitForRender() {
        int timerSleepsMillisInThread = 10;
        sleep(timerSleepsMillisInThread / 2);
    }

    protected void waitForTimerThread() {
        int timerSleepsMillisInThread = 10;
        sleep(timerSleepsMillisInThread * 2);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (@SuppressWarnings("unused") InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    public void assertFrameVisible() {
        assertEquals("Babysteps Timer", timerFrame.getTitle());
        assertTrue(timerFrame.isUndecorated());
        assertEquals(new Dimension(250, 120), timerFrame.getSize());
        assertEquals(JFrame.EXIT_ON_CLOSE, timerFrame.getDefaultCloseOperation());
        assertTrue(timerFrame.isVisible());

        assertFalse(timerPane.isEditable());
        Component[] components = timerFrame.getContentPane().getComponents();
        for (Component component : components) {
            if (component == timerPane) {
                return;
            }
        }
        fail("timerPane not in ConentPane");
    }

    public void assertOnTop(boolean onTop) {
        assertEquals(onTop, timerFrame.isAlwaysOnTop());
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
        sut.timerRunning = false;
        waitForTimerThread();
    }

    protected void resetSingleton() {
        timerFrame.setVisible(false);
        timerFrame.dispose();
        sut.timerFrame = null;
        sut.timerPane = null;
        BabystepsTimer.SECONDS_IN_CYCLE = 120; // hack to speed things up
        sut.bodyBackgroundColor = BabystepsTimer.BACKGROUND_COLOR_NEUTRAL; // singleton fix
    }

    public void waitFor(int seconds) {
        sleep(seconds * 1000);
    }

    public int secondsInCycle() {
        return (int) BabystepsTimer.SECONDS_IN_CYCLE;
    }
}
