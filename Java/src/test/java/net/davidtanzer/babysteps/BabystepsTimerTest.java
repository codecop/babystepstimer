package net.davidtanzer.babysteps;

import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.Dimension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BabystepsTimerTest {

    private JFrame timerFrame;
    private JTextPane timerPane;

    @Test
    public void shouldDisplay_RunStop_RunFinish_Reset() throws InterruptedException {
        // Display
        showTimer();
        assertInitialTimerState();

        // RunStop
        clickStart(2);
        sleep(1000);
        assertTimerMoved();
        clickStop();
        assertInitialTimerState();

        // RunFinish
        clickStart(2);
        sleep(1000);
        sleep(1000);
        assertTimerFinished();

        clickStop();

        // Reset
        clickStart(2);
        sleep(1000);

        clickReset();
        assertTimerReset();
    }

    private void showTimer() throws InterruptedException {
        BabystepsTimer.SECONDS_IN_CYCLE = 120;
        BabystepsTimer.main(new String[0]);
        timerFrame = BabystepsTimer.timerFrame;
        timerPane = BabystepsTimer.timerPane;
        sleep(100);
        clickStop();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void assertInitialTimerState() {
        assertEquals("Babysteps Timer", timerFrame.getTitle());
        assertTrue(timerFrame.isUndecorated());
        assertEquals(new Dimension(250, 120), timerFrame.getSize());
        assertTrue(timerFrame.isVisible());

        String text = "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body style=\"border-top-color: #555555; border-top-style: solid; border-top-width: 3px; border-right-color: #555555; border-right-style: solid; border-right-width: 3px; border-bottom-color: #555555; border-bottom-style: solid; border-bottom-width: 3px; border-left-color: #555555; border-left-style: solid; border-left-width: 3px; background-color: #ffffff; background-image: null; background-repeat: repeat; background-attachment: scroll; background-position: null; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0\">\n" +
                "    <h1 align=\"center\">\n" +
                "      02:00\n" +
                "    </h1>\n" +
                "    <div align=\"center\">\n" +
                "      <a href=\"command://start\"><font color=\"#555555\">Start</font></a> <a href=\"command://quit\"><font color=\"#555555\">Quit</font></a>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
        assertEquals(text, timerPane.getText());
        assertFalse(timerPane.isEditable());
    }

    private void clickStart(int seconds) {
        BabystepsTimer.SECONDS_IN_CYCLE = seconds;
        HyperlinkEvent event = new HyperlinkEvent(timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://start");
        timerPane.fireHyperlinkUpdate(event);
        sleep(100);
    }

    private void assertTimerMoved() {
        String text = "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body style=\"border-top-color: #555555; border-top-style: solid; border-top-width: 3px; border-right-color: #555555; border-right-style: solid; border-right-width: 3px; border-bottom-color: #555555; border-bottom-style: solid; border-bottom-width: 3px; border-left-color: #555555; border-left-style: solid; border-left-width: 3px; background-color: #ffffff; background-image: null; background-repeat: repeat; background-attachment: scroll; background-position: null; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0\">\n" +
                "    <h1 align=\"center\">\n" +
                "      00:01\n" +
                "    </h1>\n" +
                "    <div align=\"center\">\n" +
                "      <a href=\"command://stop\"><font color=\"#555555\">Stop</font></a> <a href=\"command://reset\"><font color=\"#555555\">Reset</font></a> \n" +
                "      <a href=\"command://quit\"><font color=\"#555555\">Quit</font></a>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
        assertEquals(text, timerPane.getText());
    }

    private void assertTimerFinished() {
        String text = "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body style=\"border-top-color: #555555; border-top-style: solid; border-top-width: 3px; border-right-color: #555555; border-right-style: solid; border-right-width: 3px; border-bottom-color: #555555; border-bottom-style: solid; border-bottom-width: 3px; border-left-color: #555555; border-left-style: solid; border-left-width: 3px; background-color: #ffcccc; background-image: null; background-repeat: repeat; background-attachment: scroll; background-position: null; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0\">\n" +
                "    <h1 align=\"center\">\n" +
                "      00:00\n" +
                "    </h1>\n" +
                "    <div align=\"center\">\n" +
                "      <a href=\"command://stop\"><font color=\"#555555\">Stop</font></a> <a href=\"command://reset\"><font color=\"#555555\">Reset</font></a> \n" +
                "      <a href=\"command://quit\"><font color=\"#555555\">Quit</font></a>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
        assertEquals(text, timerPane.getText());
    }

    private void clickReset() {
        HyperlinkEvent event = new HyperlinkEvent(timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://reset");
        timerPane.fireHyperlinkUpdate(event);
        sleep(100);
    }

    private void assertTimerReset() {
        String text = "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body style=\"border-top-color: #555555; border-top-style: solid; border-top-width: 3px; border-right-color: #555555; border-right-style: solid; border-right-width: 3px; border-bottom-color: #555555; border-bottom-style: solid; border-bottom-width: 3px; border-left-color: #555555; border-left-style: solid; border-left-width: 3px; background-color: #ccffcc; background-image: null; background-repeat: repeat; background-attachment: scroll; background-position: null; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0\">\n" +
                "    <h1 align=\"center\">\n" +
                "      00:02\n" +
                "    </h1>\n" +
                "    <div align=\"center\">\n" +
                "      <a href=\"command://stop\"><font color=\"#555555\">Stop</font></a> <a href=\"command://reset\"><font color=\"#555555\">Reset</font></a> \n" +
                "      <a href=\"command://quit\"><font color=\"#555555\">Quit</font></a>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
        assertEquals(text, timerPane.getText());
    }

    private void clickStop() {
        BabystepsTimer.SECONDS_IN_CYCLE = 120;
        HyperlinkEvent event = new HyperlinkEvent(timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://stop");
        timerPane.fireHyperlinkUpdate(event);
        sleep(100);
    }
}