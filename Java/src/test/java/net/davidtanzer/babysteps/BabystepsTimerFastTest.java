package net.davidtanzer.babysteps;

import org.junit.After;
import org.junit.Test;

/**
 * Mutation Coverage 87%: Not covered is
 * - whole MouseMotionListener
 * - quit command
 * - outer closing div (ignored by browser)
 * - TODO test ENTERED/EXITED event type does not trigger any action (all 4 actions)
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
        timer.assertFrameVisible();
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldRunAndStop() {
        timer.show();
        // run some time
        timer.clickStart();
        timer.waitFor(59);
        assertThatTimerHtml.hasMovedNeutral("01:01");
        // stop
        timer.clickStop();
        assertThatTimerHtml.isInInitialState();
        // and stay stopped
        timer.waitFor(5);
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldBeOnTopWhenRunning() {
        timer.show();
        timer.assertOnTop(false);

        timer.clickStart();
        timer.assertOnTop(true);

        timer.clickStop();
        timer.assertOnTop(false);
    }

    @Test
    public void shouldPlayGongTenSecondsToFail() {
        timer.show();
        timer.clickStart();
        timer.waitFor(timer.secondsInCycle() - 10); // only as fast test
        timer.assertAudioClipPlayed("2166__suburban-grilla__bowl-struck.wav");
    }

    @Test
    public void shouldRunToFail() {
        timer.show();
        timer.clickStart();
        timer.waitFor(timer.secondsInCycle());
        assertThatTimerHtml.isFailed();
    }

    @Test
    public void shouldPlayBellWhenFailed() {
        timer.show();
        timer.clickStart();
        timer.waitFor(timer.secondsInCycle());
        timer.assertAudioClipPlayed("32304__acclivity__shipsbell.wav");
    }

    @Test
    public void shouldResetAndStartOverAsPass() {
        timer.show();
        // run some time
        timer.clickStart();
        timer.waitFor(20);
        // reset
        timer.clickReset();
        assertThatTimerHtml.isPassed();
        // continue running
        timer.waitFor(4);
        assertThatTimerHtml.hasMovedAfterPass("01:56");
    }

    @Test
    public void shouldBecomeNeutral5SecondsAfterReset() {
        timer.show();
        timer.clickStart();
        timer.clickReset();
        timer.waitFor(5); // only as fast test
        assertThatTimerHtml.hasMovedNeutral("01:55");
    }

    @Test
    public void shouldFailAndRunOver() {
        timer.show();
        timer.clickStart();
        timer.waitFor(timer.secondsInCycle() - 1);
        assertThatTimerHtml.hasMovedNeutral("00:01");
        timer.waitFor(1);
        assertThatTimerHtml.isFailed();

        // run over
        timer.waitFor(1);
        assertThatTimerHtml.hasMovedAfterFail("02:00");
    }

    @Test
    public void shouldBecomeNeutral5SecondsAfterFail() {
        timer.show();
        timer.clickStart();
        timer.waitFor(timer.secondsInCycle());

        // run over
        timer.waitFor(1);

        timer.waitFor(5); // only as fast test
        assertThatTimerHtml.hasMovedNeutral("01:55");
    }

}
