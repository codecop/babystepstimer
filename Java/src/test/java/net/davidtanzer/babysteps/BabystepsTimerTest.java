package net.davidtanzer.babysteps;

import org.junit.After;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.Dimension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Mutation Coverage 84%: Not covered is
 * - whole MouseMotionListener
 * - quit command
 * - outer closing div (ignored by browser)
 * - elapsedTime >= SECONDS_IN_CYCLE * 1000 + 980
 * - elapsedTime >= 5000 && elapsedTime < 6000 && !BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)
 * - sounds
 */
public class BabystepsTimerTest {

    private JFrame timerFrame;
    private JTextPane timerPane;
    private final AssertTimerHtml assertTimerHtml = new AssertTimerHtml(() -> timerPane.getText());

    @After
    public void closeTimer() {
        stopTimer();
        resetSingleton();
    }

    private void stopTimer() {
        BabystepsTimer.timerRunning = false;
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

    private void resetSingleton() {
        timerFrame.setVisible(false);
        timerFrame.dispose();
        BabystepsTimer.timerFrame = null;
        BabystepsTimer.timerPane = null;
        BabystepsTimer.SECONDS_IN_CYCLE = 120; // hack to speed things up
        BabystepsTimer.bodyBackgroundColor = BabystepsTimer.BACKGROUND_COLOR_NEUTRAL; // singleton fix
    }

    @Test
    public void shouldDisplayInitialTimer() throws InterruptedException {
        showTimer();
        assertTimerFrameVisible();
        assertTimerHtml.inInitialState();
    }

    private void showTimer() throws InterruptedException {
        BabystepsTimer.main(new String[0]);
        timerFrame = BabystepsTimer.timerFrame;
        timerPane = BabystepsTimer.timerPane;
        waitForRender();
    }

    private void assertTimerFrameVisible() {
        assertEquals("Babysteps Timer", timerFrame.getTitle());
        assertTrue(timerFrame.isUndecorated());
        assertEquals(new Dimension(250, 120), timerFrame.getSize());
        assertTrue(timerFrame.isVisible());

        assertFalse(timerPane.isEditable());
    }

    @Test
    public void shouldRunStop() throws InterruptedException {
        showTimer();
        clickStart();
        waitOneSecond();
        assertTimerHtml.moved();
        clickStop();
        assertTimerHtml.inInitialState();
    }

    private void waitOneSecond() {
        sleep(1000);
    }

    private void clickStart(int... seconds) {
        if (seconds.length > 0) {
            BabystepsTimer.SECONDS_IN_CYCLE = seconds[0];
        }
        click("command://start");
        waitForRender();
    }

    private void click(String s) {
        HyperlinkEvent event = new HyperlinkEvent(timerPane, HyperlinkEvent.EventType.ACTIVATED, null, s);
        timerPane.fireHyperlinkUpdate(event);
    }

    private void clickStop() {
        BabystepsTimer.SECONDS_IN_CYCLE = 120;
        click("command://stop");
        waitForRender();
    }

    @Test
    public void shouldRunToFinish() throws InterruptedException {
        showTimer();
        clickStart(2);
        waitOneSecond();
        waitOneSecond();
        assertTimerHtml.finished();
    }

    @Test
    public void shouldResetAndContinue() throws InterruptedException {
        showTimer();
        clickStart();
        waitOneSecond();

        clickReset();
        assertTimerHtml.reset();
        waitOneSecond();
        assertTimerHtml.movedAfterReset();
    }

    private void clickReset() {
        click("command://reset");
        waitForRender();
    }

}