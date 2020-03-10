package net.davidtanzer.babysteps;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

class SwingHtmlTimerView implements TimerView {

    /* for test */ final JFrame frame;
    /* for test */ final JTextPane textPane;

    public SwingHtmlTimerView() {
        frame = new JFrame("Babysteps Timer");
        frame.setUndecorated(true);
        frame.setSize(250, 120);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.addMouseMotionListener(new MouseMotionListener() {
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

                frame.setLocation(frame.getLocation().x + (x - lastX), frame.getLocation().y + (y - lastY));

                lastX = x;
                lastY = y;
            }
        });
        frame.getContentPane().add(textPane);
        frame.setVisible(true);
    }
    
    @Override
    public void registerActionListener(TimerActionListener listener) 
    {
        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if ("command://start".equals(e.getDescription())) {
                        listener.start();
                    } else if ("command://stop".equals(e.getDescription())) {
                        listener.stop();
                    } else if ("command://reset".equals(e.getDescription())) {
                        listener.reset();
                    } else if ("command://quit".equals(e.getDescription())) {
                        listener.quit();
                    }
                }
            }
        });
    }

    @Override
    public void showTimeRunning(String time, String bodyBackgroundColor) {
        textPane.setText(createTimerHtml(time, bodyBackgroundColor, true));
        frame.repaint();
    }

    @Override
    public void showTimeStopped(String time, String bodyBackgroundColor) {
        textPane.setText(createTimerHtml(time, bodyBackgroundColor, false));
        frame.repaint();
    }

    private String createTimerHtml(final String timerText, final String bodyColor, final boolean running) {
        String timerHtml = "<html><body style=\"border: 3px solid #555555; background: " + bodyColor + "; margin: 0; padding: 0;\">" +
                "<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">" + timerText + "</h1>" +
                "<div style=\"text-align: center\">";
        if (running) {
            timerHtml += "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> " +
                    "<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
        } else {
            timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
        }
        timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
        timerHtml += "</div>" +
                "</body></html>";
        return timerHtml;
    }

    @Override
    public void setBeOnTop(boolean onTop) {
        frame.setAlwaysOnTop(onTop);
    }
}
