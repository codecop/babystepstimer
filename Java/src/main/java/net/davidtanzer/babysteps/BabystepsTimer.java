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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class BabystepsTimer {
    private static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
    private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
    private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

    static long SECONDS_IN_CYCLE = 120;

    static TimerViewImplementation timerView = new TimerViewImplementation();
    
    static class TimerViewImplementation implements TimerView {

        final JFrame timerFrame;
        final JTextPane timerPane;

        TimerViewImplementation() {
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
            timerFrame.getContentPane().add(timerPane);
            timerFrame.setVisible(true);
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
    
    static TimerListener timerListener = new TimerListener() {

        @Override
        public void start() {
            timerView.setAlwaysOnTop(true);
            timerView.showRunning(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL); 
            new TimerThread().start();
        }

        @Override
        public void stop() {
            model.timerRunning = false;
            timerView.setAlwaysOnTop(false);
            timerView.showStopped(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL);
        }

        @Override
        public void reset() {
            model.currentCycleStartTime = System.currentTimeMillis();
            model.bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
        }

        @Override
        public void quit() {
            System.exit(0);
        }
        
    };
    
    private static DecimalFormat twoDigitsFormat = new DecimalFormat("00");

    private static TimerModel model = new TimerModel(BACKGROUND_COLOR_NEUTRAL);
    
    public static void main(final String[] args) throws InterruptedException {
        timerView.showStopped(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL);
   }

    private static String getRemainingTimeCaption(final long elapsedTime) {
        long elapsedSeconds = elapsedTime / 1000;
        long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

        long remainingMinutes = remainingSeconds / 60;
        return twoDigitsFormat.format(remainingMinutes) + ":" + twoDigitsFormat.format(remainingSeconds - remainingMinutes * 60);
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            BabystepsTimer.class.getResourceAsStream("/" + url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private static final class TimerThread extends Thread {
        @Override
        public void run() {
            model.timerRunning = true;
            model.currentCycleStartTime = System.currentTimeMillis();

            while (model.timerRunning) {
                long elapsedTime = System.currentTimeMillis() - model.currentCycleStartTime;

                if (elapsedTime >= SECONDS_IN_CYCLE * 1000 + 980) {
                    model.currentCycleStartTime = System.currentTimeMillis();
                    elapsedTime = System.currentTimeMillis() - model.currentCycleStartTime;
                }
                if (elapsedTime >= 5000 && elapsedTime < 6000 && !BACKGROUND_COLOR_NEUTRAL.equals(model.bodyBackgroundColor)) {
                    model.bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
                }

                String remainingTime = getRemainingTimeCaption(elapsedTime);
                if (!remainingTime.equals(model.lastRemainingTime)) {
                    if (remainingTime.equals("00:10")) {
                        playSound("2166__suburban-grilla__bowl-struck.wav");
                    } else if (remainingTime.equals("00:00")) {
                        playSound("32304__acclivity__shipsbell.wav");
                        model.bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
                    }

                    timerView.showRunning(remainingTime, model.bodyBackgroundColor); // TODO color -> enum
                    model.lastRemainingTime = remainingTime;
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
