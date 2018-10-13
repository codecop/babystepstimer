package net.davidtanzer.babysteps;

import org.junit.After;
import org.junit.Test;

/**
 * Mutation Coverage tbd: Not covered is
 * - whole MouseMotionListener
 * - quit command
 * - outer closing div (ignored by browser)
 * - sounds
 */
public class BabystepsTimerFastTest {

    private final BabystepsTimerDriver timer = new BabystepsStubTimerDriver();
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
        timer.clickStart();
        timer.waitFor(59);
        assertThatTimerHtml.hasMoved("01:01");
        timer.clickStop();
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldPlayGongTenSecondsToFinish() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(110);
        // TODO assert playing sound "2166__suburban-grilla__bowl-struck.wav"
    }

    @Test
    public void shouldRunToFinish() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(120);
        assertThatTimerHtml.isFinished();
        // TODO assert playing sound "32304__acclivity__shipsbell.wav"
    }

    @Test
    public void shouldResetAndContinue() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(20);
        timer.clickReset();
        assertThatTimerHtml.wasReset();
        timer.waitFor(4);
        assertThatTimerHtml.hasMovedAfterReset("01:56");
    }

    @Test
    public void shouldBecomeWhite5SecondsAfterReset() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.clickReset();
        timer.waitFor(5);
        assertThatTimerHtml.hasMoved("01:55");
    }

}