package net.davidtanzer.babysteps;

import org.junit.After;
import org.junit.Test;

/**
 * Basic tests which run slow.<p>
 * Mutation Coverage 84%: Not covered is
 * - whole MouseMotionListener
 * - quit command
 * - outer closing div (ignored by browser)
 * - elapsedTime >= SECONDS_IN_CYCLE * 1000 + 980
 * - elapsedTime >= 5000 && elapsedTime < 6000 && !BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)
 * - sounds
 */
public class BabystepsTimerTest {

    private final BabystepsTimerDriver timer = new BabystepsTimerDriver();
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
    public void shouldRunAndStop() {
        // in fact two tests initial->run, running->stop
        timer.show();
        timer.start();
        timer.waitFor(1);
        assertThatTimerHtml.hasMoved("01:59");
        timer.stop();
        assertThatTimerHtml.isInInitialState();
    }

    @Test
    public void shouldRunToFail() {
        timer.show();
        timer.start(2);
        timer.waitFor(timer.secondsInCycle());
        assertThatTimerHtml.isFailed();
    }

    @Test
    public void shouldResetAndStartOverAsPass() {
        timer.show();
        timer.start();
        timer.waitFor(1);
        timer.reset();
        assertThatTimerHtml.isPassed();
        timer.waitFor(1);
        assertThatTimerHtml.hasMovedAfterPass("01:59");
    }

}
