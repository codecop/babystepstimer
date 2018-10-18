/*  Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package net.davidtanzer.babysteps;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class BabystepsTimer {

    /* for slow test */ static long SECONDS_IN_CYCLE = 120;

    /* for test */ final JFrame timerFrame;
    /* for test */ final JTextPane timerPane;

    // TODO volatile/thread safe, accessed/written from two threads
    /* for test */ boolean timerRunning;
    private long currentCycleStartTime;
    
    private final Timer timer;
    private final BabystepsSignal signal;
    
    class UI {
        private static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
        private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
        private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

        String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;

        public void showNormal() {
            bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
        }


        public void showFailure() {
            bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
        }

        public void showPassed() {
            bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
        }
        
        public boolean isNormal() {
            return BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor);
        }
     
        public String createTimerHtml(final String timerText, final boolean running) {
            final String bodyColor = bodyBackgroundColor;
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
    
    private final UI ui = new UI();

    public static void main(final String[] args) {
        new BabystepsTimer();
    }
    
    public BabystepsTimer() {
        this(new SystemTimer(), new AudioSignal(new SampledAudioClip()));
    }
    
    /* for test */ BabystepsTimer(final Timer timer, final BabystepsSignal signal) {
        this.timer = timer;
        this.signal = signal;
        
        timerFrame = new JFrame("Babysteps Timer");
        timerFrame.setUndecorated(true);

        timerFrame.setSize(250, 120);
        timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timerPane = new JTextPane();
        timerPane.setContentType("text/html");
        String timerText = getRemainingTimeCaption(0L);
        timerPane.setText(ui.createTimerHtml(timerText, false));
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
        timerPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if ("command://start".equals(e.getDescription())) {
                        timerFrame.setAlwaysOnTop(true);
                        String timerText = getRemainingTimeCaption(0L);
                        timerPane.setText(ui.createTimerHtml(timerText, true));
                        timerFrame.repaint();
                        new TimerThread().start();
                    } else if ("command://stop".equals(e.getDescription())) {
                        timerRunning = false;
                        timerFrame.setAlwaysOnTop(false);
                        ui.showNormal();
                        String timerText = getRemainingTimeCaption(0L);
                        timerPane.setText(ui.createTimerHtml(timerText, false));
                        timerFrame.repaint();
                    } else if ("command://reset".equals(e.getDescription())) {
                        currentCycleStartTime = timer.getTime();
                        ui.showPassed();
                    } else if ("command://quit".equals(e.getDescription())) {
                        // TODO not covered
                        System.exit(0);
                    }
                }
            }
        });
        timerFrame.getContentPane().add(timerPane);

        timerFrame.setVisible(true);
    }

    private String getRemainingTimeCaption(final long elapsedTime) {
        long elapsedSeconds = elapsedTime / 1000;
        long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

        long remainingMinutes = remainingSeconds / 60;
        DecimalFormat twoDigitsFormat = new DecimalFormat("00");
        return twoDigitsFormat.format(remainingMinutes) + ":" + twoDigitsFormat.format(remainingSeconds - remainingMinutes * 60);
    }

    private final class TimerThread extends Thread {
        private String lastRemainingTime;

        @Override
        public void run() {
            timerRunning = true;
            currentCycleStartTime = timer.getTime();

            while (timerRunning) {
                long elapsedTime = timer.getTime() - currentCycleStartTime;

                if (elapsedTime >= SECONDS_IN_CYCLE * 1000 + 980) {
                    currentCycleStartTime = timer.getTime();
                    elapsedTime = timer.getTime() - currentCycleStartTime;
                }

                String remainingTime = getRemainingTimeCaption(elapsedTime);
                if (!remainingTime.equals(lastRemainingTime)) {

                    if (elapsedTime >= 5 * 1000 && elapsedTime < 5 * 1000 + 1000 && !ui.isNormal()) {
                        ui.showNormal();
                    } else if (elapsedTime >= (SECONDS_IN_CYCLE - 10) * 1000 && elapsedTime < (SECONDS_IN_CYCLE - 10) * 1000 + 1000) {
                        signal.warning();
                    } else if (elapsedTime >= SECONDS_IN_CYCLE * 1000) {
                        signal.failure();
                        ui.showFailure();
                    }

                    SwingUtilities.invokeLater(() -> {
                        timerPane.setText(ui.createTimerHtml(remainingTime, true));
                        timerFrame.repaint();
                    });

                    lastRemainingTime = remainingTime;
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    //We don't really care about this one...
                }
            }
        }
    }

    public void close() {
        timerFrame.setVisible(false);
        timerFrame.dispose();
    }

}
