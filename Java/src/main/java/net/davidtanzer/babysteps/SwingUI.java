package net.davidtanzer.babysteps;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class SwingUI implements UI {

    private static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
    private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
    private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

    /* for test */ JFrame timerFrame;
    /* for test */ JTextPane timerPane;
    private String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;

    @Override
    public void create() {
        timerFrame = new JFrame("Babysteps Timer");
        timerFrame.setUndecorated(true);
        timerFrame.setSize(250, 120);
        timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timerPane = new JTextPane();
        timerPane.setContentType("text/html");
        timerPane.setEditable(false);
        timerPane.addMouseMotionListener(new MouseMotionListener() {
            // TODO not covered

            private int lastX;
            private int lastY;

            @Override
            public void mouseMoved(final MouseEvent e) {
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();

                timerFrame.setLocation(timerFrame.getLocation().x + (x - lastX), timerFrame.getLocation().y + (y - lastY));

                lastX = x;
                lastY = y;
            }
        });

        timerFrame.getContentPane().add(timerPane);
    }

    @Override
    public void setActions(final BabystepsActions actions) {
        timerPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if ("command://start".equals(e.getDescription())) {
                        actions.start();
                    } else if ("command://stop".equals(e.getDescription())) {
                        actions.stop();
                    } else if ("command://reset".equals(e.getDescription())) {
                        actions.reset();
                    } else if ("command://quit".equals(e.getDescription())) {
                        actions.quit();
                    }
                }
            }
        });
    }

    @Override
    public void display() {
        timerFrame.setVisible(true);
    }

    @Override
    public void onTop() {
        timerFrame.setAlwaysOnTop(true);
    }

    @Override
    public void notOnTop() {
        timerFrame.setAlwaysOnTop(false);
    }

    @Override
    public void showNormal() {
        bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
    }

    @Override
    public void showFailure() {
        bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
    }

    @Override
    public void showPassed() {
        bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
    }

    @Override
    public boolean isNormal() {
        return BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor);
    }

    @Override
    public void showTime(final String timerText, final boolean running) {
        Runnable update = () -> timerPane.setText(createTimerHtml(timerText, running));
        invokeOnEventThread(update);
    }

    private String createTimerHtml(final String timerText, final boolean running) {
        final String bodyColor = bodyBackgroundColor;
        String timerHtml = "<html><body style=\"border: 3px solid #555555; background: " + bodyColor + "; margin: 0; padding: 0;\">"
                + "<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">" + timerText + "</h1>"
                + "<div style=\"text-align: center\">";
        if (running) {
            timerHtml += "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> "
                    + "<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
        } else {
            timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
        }
        timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
        timerHtml += "</div>" + "</body></html>";
        return timerHtml;
    }

    private void invokeOnEventThread(Runnable update) {
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        } else {
            invokeAndWait(update);
        }
    }

    private void invokeAndWait(Runnable update) {
        try {

            SwingUtilities.invokeAndWait(update);

        } catch (@SuppressWarnings("unused") InterruptedException ignored) {
            Thread.currentThread().interrupt();

        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        timerFrame.setVisible(false);
        timerFrame.dispose();
    }

}