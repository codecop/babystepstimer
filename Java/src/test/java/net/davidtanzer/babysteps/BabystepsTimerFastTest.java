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
    public void shouldRunToFinish() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(120);
        assertThatTimerHtml.isFinished();
        // TODO assert playing sound "32304__acclivity__shipsbell.wav"
    }

    @Test
    public void shouldPlayGongTenSecondsToFinish() throws InterruptedException {
        timer.show();
        timer.clickStart();
        timer.waitFor(110);
        // TODO assert playing sound "2166__suburban-grilla__bowl-struck.wav"
    }

//    @Test
//    public void shouldResetAndContinue() throws InterruptedException {
//        timer.show();
//        timer.clickStart();
//        timer.waitFor(1);
//
//        timer.clickReset();
//        assertThatTimerHtml.wasReset();
//        timer.waitFor(1);
//        assertThatTimerHtml.hasMovedAfterReset();
//    }

}