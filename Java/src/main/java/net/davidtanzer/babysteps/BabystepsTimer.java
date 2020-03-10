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

public class BabystepsTimer implements TimerListener, Runnable {
    private static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
    private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
    private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

    /* for test */ static TimerView timerView = new SwingHtmlTimerView();
    private final DecimalFormat twoDigitsFormat = new DecimalFormat("00");
    /* for test */ static long SECONDS_IN_CYCLE = 120;

    private String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
    private boolean timerRunning;
    private long currentCycleStartTime;
    private String lastRemainingTime;

    public static void main(final String[] args) throws InterruptedException {
        new BabystepsTimer(timerView);
    }

    public BabystepsTimer(TimerView timerView) {
        timerView.register(this);
        timerView.showStopped(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL);
    }

    @Override
    public void start() {
        timerView.setAlwaysOnTop(true);
        timerView.showRunning(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL); 
        new Thread(this).start();
    }

    @Override
    public void stop() {
        timerRunning = false;
        timerView.setAlwaysOnTop(false);
        timerView.showStopped(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL);
    }

    @Override
    public void reset() {
        currentCycleStartTime = System.currentTimeMillis();
        bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
    }

    @Override
    public void quit() {
        System.exit(0);
    }

    private String getRemainingTimeCaption(final long elapsedTime) {
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

    @Override
    public void run() {
        timerRunning = true;
        currentCycleStartTime = System.currentTimeMillis();

        while (timerRunning) {
            long elapsedTime = System.currentTimeMillis() - currentCycleStartTime;

            if (elapsedTime >= SECONDS_IN_CYCLE * 1000 + 980) {
                currentCycleStartTime = System.currentTimeMillis();
                elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
            }
            if (elapsedTime >= 5000 && elapsedTime < 6000 && !BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)) {
                bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
            }

            String remainingTime = getRemainingTimeCaption(elapsedTime);
            if (!remainingTime.equals(lastRemainingTime)) {
                if (remainingTime.equals("00:10")) {
                    playSound("2166__suburban-grilla__bowl-struck.wav");
                } else if (remainingTime.equals("00:00")) {
                    playSound("32304__acclivity__shipsbell.wav");
                    bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
                }

                timerView.showRunning(remainingTime, bodyBackgroundColor); // TODO color -> enum
                lastRemainingTime = remainingTime;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                //We don't really care about this one...
            }
        }
    }
}
