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
    public void shouldDisplayInitialTimer() throws InterruptedException {
        timer.show();
        timer.assertFrameVisible();
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldRunAndStop() throws InterruptedException {
        timer.show();
        // run some time
        timer.clickStart();
        timer.waitFor(59);
        assertThatTimerHtml.hasMoved("01:01");
        // stop
        timer.clickStop();
        assertThatTimerHtml.isInInitialState();
        // and stay stopped
        timer.waitFor(5);
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldBeOnTopWhenRunning() throws InterruptedException {
        timer.show();
        timer.assertOnTop(false);
        timer.clickStart();
        timer.assertOnTop(true);
        timer.clickStop();
        timer.assertOnTop(false);
    }

    @Test
    public void shouldPlayGongTenSecondsToFinish() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(110); // only as fast test
        timer.assertAudioClipPlayed("2166__suburban-grilla__bowl-struck.wav");
    }

    @Test
    public void shouldRunToFinish() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(120);
        assertThatTimerHtml.isFinished();
    }

    @Test
    public void shouldPlayBellWhenFinished() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(120);
        timer.assertAudioClipPlayed("32304__acclivity__shipsbell.wav");
    }

    @Test
    public void shouldResetAndContinue() throws InterruptedException {
        timer.show();
        // run some time
        timer.clickStart();
        timer.waitFor(20);
        // reset
        timer.clickReset();
        assertThatTimerHtml.wasReset();
        // continue running
        timer.waitFor(4);
        assertThatTimerHtml.hasMovedAfterReset("01:56");
    }

    @Test
    public void shouldBecomeWhite5SecondsAfterReset() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.clickReset();
        timer.waitFor(5); // only as fast test
        assertThatTimerHtml.hasMoved("01:55");
    }

    @Test
    public void shouldRunOver() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(119);
        assertThatTimerHtml.hasMoved("00:01");
        timer.waitFor(1);
        assertThatTimerHtml.isFinished();
        timer.waitFor(1);
        assertThatTimerHtml.hasOverrun("02:00");
    }

    @Test
    public void shouldBecomeWhite5SecondsAfterRunOver() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(120);
        timer.waitFor(1);
        timer.waitFor(5); // only as fast test
        assertThatTimerHtml.hasMoved("01:55");
    }

}
