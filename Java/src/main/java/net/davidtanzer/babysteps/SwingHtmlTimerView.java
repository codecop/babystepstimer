package net.davidtanzer.babysteps;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

class SwingHtmlTimerView implements TimerView {

    final JFrame timerFrame;
    final JTextPane timerPane;

    public SwingHtmlTimerView() {
        timerFrame = new JFrame("Babysteps Timer");
        timerFrame.setUndecorated(true);

        timerFrame.setSize(250, 120);
        timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        timerPane = new JTextPane();
        timerPane.setContentType("text/html");
        timerPane.setEditable(false);
        timerPane.addMouseMotionListener(new MouseMotionListener() {
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
        timerFrame.setVisible(true);
    }
    
    @Override
    public void register(TimerListener timerListener) 
    {
        timerPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if ("command://start".equals(e.getDescription())) {
                        timerListener.start();
                    } else if ("command://stop".equals(e.getDescription())) {
                        timerListener.stop();
                    } else if ("command://reset".equals(e.getDescription())) {
                        timerListener.reset();
                    } else if ("command://quit".equals(e.getDescription())) {
                        timerListener.quit();
                    }
                }
            }
        });
    }

    @Override
    public void showRunning(String time, String bodyBackgroundColor) {
        timerPane.setText(createTimerHtml(time, bodyBackgroundColor, true));
        timerFrame.repaint();
    }

    @Override
    public void showStopped(String time, String bodyBackgroundColor) {
        timerPane.setText(createTimerHtml(time, bodyBackgroundColor, false));
        timerFrame.repaint();
    }

    @Override
    public void setAlwaysOnTop(boolean b) {
        timerFrame.setAlwaysOnTop(b);
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
}
