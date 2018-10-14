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
    static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
    private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
    private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

    static long SECONDS_IN_CYCLE = 120;

    static JFrame timerFrame;
    static JTextPane timerPane;
    // TODO volatile, accessed from two threads
    static boolean timerRunning;
    // TODO volatile, accessed from two threads
    private static long currentCycleStartTime;
    static String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
    static Timer timer = new SystemTimer();
    static AudioClip audioClip = new SampledAudioClip();

    public static void main(final String[] args) {
        timerFrame = new JFrame("Babysteps Timer");
        timerFrame.setUndecorated(true);

        timerFrame.setSize(250, 120);
        timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timerPane = new JTextPane();
        timerPane.setContentType("text/html");
        timerPane.setText(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, false));
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
                        timerPane.setText(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, true));
                        timerFrame.repaint();
                        new TimerThread().start();
                    } else if ("command://stop".equals(e.getDescription())) {
                        timerRunning = false;
                        timerFrame.setAlwaysOnTop(false);
                        timerPane.setText(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, false));
                        timerFrame.repaint();
                    } else if ("command://reset".equals(e.getDescription())) {
                        currentCycleStartTime = timer.getTime();
                        bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
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

    private static String getRemainingTimeCaption(final long elapsedTime) {
        long elapsedSeconds = elapsedTime / 1000;
        long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

        long remainingMinutes = remainingSeconds / 60;
        DecimalFormat twoDigitsFormat = new DecimalFormat("00");
        return twoDigitsFormat.format(remainingMinutes) + ":" + twoDigitsFormat.format(remainingSeconds - remainingMinutes * 60);
    }

    private static String createTimerHtml(final String timerText, final String bodyColor, final boolean running) {
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

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                audioClip.play(url);
            }
        }).start();
    }

    private static final class TimerThread extends Thread {
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

                    if (elapsedTime >= 5 * 1000 && elapsedTime < 5 * 1000 + 1000 && !BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)) {
                        bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
                    } else if (elapsedTime >= (SECONDS_IN_CYCLE - 10) * 1000 && elapsedTime < (SECONDS_IN_CYCLE - 10) * 1000 + 1000) {
                        playSound("2166__suburban-grilla__bowl-struck.wav");
                    } else if (elapsedTime >= SECONDS_IN_CYCLE * 1000) {
                        playSound("32304__acclivity__shipsbell.wav");
                        bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
                    }

                    SwingUtilities.invokeLater(() -> {
                        timerPane.setText(createTimerHtml(remainingTime, bodyBackgroundColor, true));
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
}
