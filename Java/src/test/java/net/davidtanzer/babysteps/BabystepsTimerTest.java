package net.davidtanzer.babysteps;

import org.junit.Test;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

import java.awt.*;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

public class BabystepsTimerTest {

    private JFrame timerFrame;
    private JTextPane timerPane;

    @Test
    public void shouldDoAll() throws InterruptedException, MalformedURLException {
        showTimer();
        assertInitialTimerState();

        clickStart();
        sleep(1000);
        assertTimerMoved();
    }

    private void showTimer() throws InterruptedException {
        BabystepsTimer.main(new String[0]);
        timerFrame = BabystepsTimer.timerFrame;
        timerPane = BabystepsTimer.timerPane;
        sleep(100);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void assertInitialTimerState() {
        String title = "Babysteps Timer";
        assertEquals(title, timerFrame.getTitle());

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
        Color foreground = new Color(51, 51, 51);
        assertEquals(foreground, timerPane.getForeground());
        Color background = new Color(0xff, 0xff, 0xff);
        assertEquals(background, timerPane.getBackground());
    }

    private void clickStart() {
        HyperlinkEvent event = new HyperlinkEvent(timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://start");
        BabystepsTimer.timerPane.fireHyperlinkUpdate(event);
    }

    private void assertTimerMoved() {
    }
}