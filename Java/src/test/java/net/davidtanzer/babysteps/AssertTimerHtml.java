package net.davidtanzer.babysteps;

import java.util.function.Supplier;

public class AssertTimerHtml {

    public static final String WHITE = "#ffffff";
    public static final String RED = "#ffcccc";
    public static final String GREEN = "#ccffcc";

    private final Supplier<String> html;

    public AssertTimerHtml(Supplier<String> html) {
        this.html = html;
    }

    public void isInInitialState() {
        String time = "02:00";
        String backgroundColor = WHITE;
        String links = startLinkHtml() + " " + quitLinkHtml() + "\n";
        assertTimerHtml(time, backgroundColor, links);
    }

    public void hasMoved(String time) {
        String backgroundColor = WHITE;
        assertRunningTimerHtml(time, backgroundColor);
    }

    public void isFinished() {
        hasOverrun("00:00");
    }

    public void hasOverrun(String time) {
        String backgroundColor = RED;
        assertRunningTimerHtml(time, backgroundColor);
    }

    public void wasReset() {
        hasMovedAfterReset("02:00");
    }

    public void hasMovedAfterReset(String time) {
        String backgroundColor = GREEN;
        assertRunningTimerHtml(time, backgroundColor);
    }

    private void assertRunningTimerHtml(String time, String backgroundColor) {
        String links = stopLinkHtml() + " " + resetLinkHtml() + " \n" + "      " + quitLinkHtml() + "\n";
        assertTimerHtml(time, backgroundColor, links);
    }

    private void assertTimerHtml(String time, String backgroundColor, String links) {
        String expectedHtml = expectedHtmlWith(time, backgroundColor, links);
        RetryAssert.assertEquals(expectedHtml, this.html, 20);
    }

    private String expectedHtmlWith(String time, String backgroundColor, String links) {
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

    private String startLinkHtml() {
        return "<a href=\"command://start\"><font color=\"#555555\">Start</font></a>";
    }

    private String quitLinkHtml() {
        return "<a href=\"command://quit\"><font color=\"#555555\">Quit</font></a>";
    }

    private String resetLinkHtml() {
        return "<a href=\"command://reset\"><font color=\"#555555\">Reset</font></a>";
    }

    private String stopLinkHtml() {
        return "<a href=\"command://stop\"><font color=\"#555555\">Stop</font></a>";
    }
}
