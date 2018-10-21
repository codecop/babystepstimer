package net.davidtanzer.babysteps;

import org.junit.After;
import org.junit.Test;

/**
 * Complete tests which run fast but do not execute timer and audio code.<p>
 * Mutation Coverage 87%: Not covered is
 * - whole MouseMotionListener
 * - quit command
 * - outer closing div (ignored by browser)
 * - ENTERED/EXITED event type should not trigger any action (all 4 actions)
 */
public class BabystepsTimerFastTest {

    private final BabystepsStubTimerDriver timer = new BabystepsStubTimerDriver();
    private final AssertTimerHtml assertThatTimerHtml = new AssertTimerHtml(timer::getHtml);

    @After
    public void closeTimer() {
        timer.close();
    }

    @Test
    public void shouldDisplayInitialTimer() {
        timer.show();
        timer.assertFrameNameSizeAndIsVisible();
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldRun() {
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle() - 1);
        assertThatTimerHtml.hasMoved("00:01");
    }

    @Test
    public void shouldStopWhenRunning() {
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle() - 1);
        // stop
        timer.stop();
        assertThatTimerHtml.isInInitialState();
        // and stay stopped
        timer.waitFor(5);
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldBeOnTopWhileRunning() {
        timer.show();
        timer.assertOnTop(false);

        timer.start();
        timer.assertOnTop(true);

        timer.stop();
        timer.assertOnTop(false);
    }

    @Test
    public void shouldPlayGongTenSecondsBeforeFailing() {
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle() - 10); // only as fast test
        timer.assertAudioClipPlayed("2166__suburban-grilla__bowl-struck.wav");
    }

    @Test
    public void shouldRunToFail() {
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle());
        assertThatTimerHtml.isFailed();
    }

    @Test
    public void shouldPlayBellWhenFailed() {
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle());
        timer.assertAudioClipPlayed("32304__acclivity__shipsbell.wav");
    }

    @Test
    public void shouldRunOverWhenFailed() {
        // run to fail
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle());
        // run over
        timer.waitFor(1);
        assertThatTimerHtml.hasMovedAfterFail("02:00");
    }

    @Test
    public void shouldBecomeNeutral5SecondsAfterFail() {
        timer.show();
        timer.start();
        timer.waitFor(timer.secondsInCycle());

        // run over
        timer.waitFor(1);

        timer.waitFor(5); // only as fast test
        assertThatTimerHtml.hasMoved("01:55");
    }

    @Test
    public void shouldResetAndStartOverAsPass() {
        timer.show();
        // run some time
        timer.start();
        timer.waitFor(20);
        // reset
        timer.reset();
        assertThatTimerHtml.isPassed();
        // continue running
        timer.waitFor(4);
        assertThatTimerHtml.hasMovedAfterPass("01:56");
    }

    @Test
    public void shouldBecomeNeutral5SecondsAfterReset() {
        timer.show();
        timer.start();
        timer.reset();
        timer.waitFor(5); // only as fast test
        assertThatTimerHtml.hasMoved("01:55");
    }

}
