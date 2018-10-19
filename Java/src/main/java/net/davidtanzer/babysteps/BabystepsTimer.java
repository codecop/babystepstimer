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

public class BabystepsTimer {

    /* for slow test */ static long SECONDS_IN_CYCLE = 120;

    // TODO volatile/thread safe, accessed/written from two threads
    /* for test */ boolean timerRunning;

    private final SystemClock clock;
    private final BabystepsSignal signal;
    /* for test */ final BabystepsUI ui;

    public static void main(final String[] args) {
        new BabystepsTimer();
    }

    public BabystepsTimer() {
        this(new SystemClock(new SystemTimer()), new AudioSignal(new SampledAudioClip()));
    }

    /* for test */ BabystepsTimer(final SystemClock clock, final BabystepsSignal signal) {
        this.clock = clock;
        this.signal = signal;
        this.ui = new SwingUI();

        BabystepsActions actions = new BabystepsActions() {
            @Override
            public void start() {
                ui.onTop();
                String timerText = getRemainingTimeCaption(0L);
                ui.showTime(timerText, true);
                new TimerThread().start();
            }

            @Override
            public void stop() {
                timerRunning = false;
                ui.notOnTop();
                ui.showNormal();
                String timerText = getRemainingTimeCaption(0L);
                ui.showTime(timerText, false);
            }

            @Override
            public void reset() {
                clock.resetCycle();
                ui.showPassed();
                String timerText = getRemainingTimeCaption(0L);
                ui.showTime(timerText, true);
            }

            @Override
            public void quit() {
                // TODO not covered
                System.exit(0);
            }
        };

        ui.create();

        String timerText = getRemainingTimeCaption(0L);
        ui.showTime(timerText, false);

        ui.setActions(actions);
        ui.display();
    }

    private String getRemainingTimeCaption(final long elapsedTime) {
        long elapsedSeconds = elapsedTime / 1000;
        long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

        long remainingMinutes = remainingSeconds / 60;
        DecimalFormat twoDigitsFormat = new DecimalFormat("00");
        return twoDigitsFormat.format(remainingMinutes) + ':' + twoDigitsFormat.format(remainingSeconds - remainingMinutes * 60);
    }

    private final class TimerThread extends Thread {
        private String lastRemainingTime;

        @Override
        public void run() {
            timerRunning = true;
            clock.resetCycle();

            while (timerRunning) {
                ElapsedSeconds elapsedSeconds = clock.getElapsedTime();

                if (elapsedSeconds.isMoreOrEqual(SECONDS_IN_CYCLE, 980)) {
                    clock.resetCycle();
                    elapsedSeconds = clock.getElapsedTime();
                }

                String remainingTime = getRemainingTimeCaption(elapsedSeconds.millis);
                if (!remainingTime.equals(lastRemainingTime)) {

                    if (elapsedSeconds.isBetween(5, 6) && !ui.isNormal()) {
                        ui.showNormal();
                    } else if (elapsedSeconds.isBetween(SECONDS_IN_CYCLE - 10, SECONDS_IN_CYCLE - 9)) {
                        signal.warning();
                    } else if (elapsedSeconds.isMoreOrEqual(SECONDS_IN_CYCLE)) {
                        signal.failure();
                        ui.showFailure();
                    }
                    ui.showTime(remainingTime, true);

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
        ui.destroy();
    }

}
