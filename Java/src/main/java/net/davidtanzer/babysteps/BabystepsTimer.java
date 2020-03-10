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

import java.text.DecimalFormat;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BabystepsTimer {
    private static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
    private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
    private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

    static long SECONDS_IN_CYCLE = 120;

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
    static TimerView timerView = new SwingHtmlTimerView(timerListener);
    
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
