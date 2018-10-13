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
        BabystepsTimer.SECONDS_IN_CYCLE = 120;
        BabystepsTimer.bodyBackgroundColor = BabystepsTimer.BACKGROUND_COLOR_NEUTRAL;
    }

    @Test
    public void shouldDisplayInitialTimer() throws InterruptedException {
        showTimer();
        assertTimerFrameVisible();
        assertTimerInInitialState();
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

    private void assertTimerInInitialState() {
        String time = "02:00";
        String backgroundColor = "#ffffff";
        String links = startLinkHtml() + " " + quitLinkHtml() + "\n";
        String text = html(time, backgroundColor, links);
        assertEquals(text, timerPane.getText());
    }

    private String startLinkHtml() {
        return "<a href=\"command://start\"><font color=\"#555555\">Start</font></a>";
    }

    private String quitLinkHtml() {
        return "<a href=\"command://quit\"><font color=\"#555555\">Quit</font></a>";
    }

    private String html(String time, String backgroundColor, String links) {
        return "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body style=\"border-top-color: #555555; border-top-style: solid; border-top-width: 3px; border-right-color: #555555; border-right-style: solid; border-right-width: 3px; border-bottom-color: #555555; border-bottom-style: solid; border-bottom-width: 3px; border-left-color: #555555; border-left-style: solid; border-left-width: 3px; background-color: " + backgroundColor + "; background-image: null; background-repeat: repeat; background-attachment: scroll; background-position: null; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0\">\n" +
                "    <h1 align=\"center\">\n" +
                "      " + time + "\n" +
                "    </h1>\n" +
                "    <div align=\"center\">\n" +
                "      " + links +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }

    @Test
    public void shouldRunStop() throws InterruptedException {
        showTimer();
        clickStart();
        waitOneSecond();
        assertTimerMoved();
        clickStop();
        assertTimerInInitialState();
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

    private void assertTimerMoved() {
        String time = "01:59";
        String backgroundColor = "#ffffff";
        String links = stopLinkHtml() + " " + resetLinkHtmk() + " \n" + "      " + quitLinkHtml() + "\n";
        String text = html(time, backgroundColor, links);
        assertEquals(text, timerPane.getText());
    }

    private String resetLinkHtmk() {
        return "<a href=\"command://reset\"><font color=\"#555555\">Reset</font></a>";
    }

    private String stopLinkHtml() {
        return "<a href=\"command://stop\"><font color=\"#555555\">Stop</font></a>";
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
        assertTimerFinished();
    }

    private void assertTimerFinished() {
        String time = "00:00";
        String backgroundColor = "#ffcccc";
        String links = stopLinkHtml() + " " + resetLinkHtmk() + " \n" + "      " + quitLinkHtml() + "\n";
        String text = html(time, backgroundColor, links);
        assertEquals(text, timerPane.getText());
    }

    @Test
    public void shouldResetAndContinue() throws InterruptedException {
        showTimer();
        clickStart();
        waitOneSecond();

        clickReset();
        assertTimerReset();
        waitOneSecond();
        assertTimerMovedAfterReset();
    }

    private void clickReset() {
        click("command://reset");
        waitForRender();
    }

    private void assertTimerReset() {
        String time = "02:00";
        String backgroundColor = "#ccffcc";
        String links = stopLinkHtml() + " " + resetLinkHtmk() + " \n" + "      " + quitLinkHtml() + "\n";
        String text = html(time, backgroundColor, links);
        assertEquals(text, timerPane.getText());
    }

    private void assertTimerMovedAfterReset() {
        String time = "01:59";
        String backgroundColor = "#ccffcc";
        String links = stopLinkHtml() + " " + resetLinkHtmk() + " \n" + "      " + quitLinkHtml() + "\n";
        String text = html(time, backgroundColor, links);
        assertEquals(text, timerPane.getText());
    }
}