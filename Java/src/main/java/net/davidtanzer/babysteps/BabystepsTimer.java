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

/**
 * Business logic of the timer.
 */
public class BabystepsTimer implements BabystepsActions, RepeatingTask {

    /* for slow test */ static long SECONDS_IN_CYCLE = 120;

    private final BabystepsClock clock;
    private final BabystepsSignal signal;
    /* for test */ final BabystepsUI ui;

    private final String initialTimeCaption;
    private boolean timerRunning;
    private String lastTimerCaption;

    public static void main(final String[] args) {
        new BabystepsTimer();
    }

    public BabystepsTimer() {
        this(new SystemClock(new SystemTimer()), new AudioSignal(new SampledAudioClip()));
    }

    /* for test */ BabystepsTimer(final BabystepsClock clock, final BabystepsSignal signal) {
        this.clock = clock;
        this.signal = signal;
        this.ui = new SwingUI();

        initialTimeCaption = getRemainingTimeCaption(ElapsedSeconds.NONE);

        ui.create();
        ui.showTime(initialTimeCaption, false);
        ui.setActions(this);
        ui.display();
    }

    @Override
    public void start() {
        setRunning(true);
        ui.onTop();
        ui.showTime(initialTimeCaption, true);
        clock.resetCycle();
        new RepeatingTaskThread(this, 10).start();
    }

    @Override
    public void stop() {
        setRunning(false);
        ui.notOnTop();
        ui.showNormal();
        ui.showTime(initialTimeCaption, false);
    }

    @Override
    public void reset() {
        clock.resetCycle();
        ui.showPassed();
        ui.showTime(initialTimeCaption, true);
    }

    @Override
    public void quit() {
        // TODO not covered
        System.exit(0);
    }

    @Override
    public synchronized boolean isRunning() {
        return timerRunning;
    }

    /* for test */ synchronized void setRunning(boolean state) {
        timerRunning = state;
    }

    @Override
    public void tick() {
        ElapsedSeconds elapsedSeconds = clock.getElapsedTime();

        boolean hasRunOver = elapsedSeconds.isMoreOrEqual(SECONDS_IN_CYCLE, 980);
        if (hasRunOver) {
            clock.resetCycle();
            elapsedSeconds = clock.getElapsedTime();
        }

        String remainingTime = getRemainingTimeCaption(elapsedSeconds);
        if (!remainingTime.equals(lastTimerCaption)) {

            if (elapsedSeconds.isBetween(5, 6) && !ui.isNormal()) {
                ui.showNormal();
            } else if (elapsedSeconds.isBetween(SECONDS_IN_CYCLE - 10, SECONDS_IN_CYCLE - 9)) {
                signal.warning();
            } else if (elapsedSeconds.isMoreOrEqual(SECONDS_IN_CYCLE)) {
                signal.failure();
                ui.showFailure();
            }
            ui.showTime(remainingTime, true);

            lastTimerCaption = remainingTime;
        }
    }

    private String getRemainingTimeCaption(final ElapsedSeconds elapsedTime) {
        long remainingSeconds = SECONDS_IN_CYCLE - elapsedTime.getSeconds();

        long remainingMinutes = remainingSeconds / 60;
        DecimalFormat twoDigitsFormat = new DecimalFormat("00");
        return twoDigitsFormat.format(remainingMinutes) + ':' + twoDigitsFormat.format(remainingSeconds - remainingMinutes * 60);
    }

    public void close() {
        ui.destroy();
    }

}
