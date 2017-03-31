package net.davidtanzer.babysteps;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.ClipboardReporter;
import org.approvaltests.reporters.ImageWebReporter;
import org.approvaltests.reporters.QuietReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.net.MalformedURLException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BabystepsTimer.class})
public class BabystepsTimerSystemTest {
    private JFrame timerFrame;

    @Before
    public void createTimer() throws InterruptedException {
        PowerMockito.mockStatic(System.class);
        Mockito.when(System.currentTimeMillis()).thenReturn(0l).thenReturn(2 * 59 * 1000l);

        BabystepsTimer babystepsTimer = new BabystepsTimer();
        babystepsTimer.main(new String[0]);
        timerFrame = BabystepsTimer.timerFrame;
        Thread.sleep(3000);
    }

    private void startTimer() throws InterruptedException {
        send("command://start");
        Thread.sleep(1500);
    }

    private void send(String desc) {
        BabystepsTimer.timerPane.getHyperlinkListeners()[0].hyperlinkUpdate(new HyperlinkEvent(BabystepsTimer.timerPane, HyperlinkEvent.EventType.ACTIVATED, null, desc));
    }

    @After
    public void closeTimer() throws InterruptedException {
        if (timerFrame != null) {
            timerFrame.setVisible(false);
            timerFrame.dispose();
            Thread.sleep(1500);
        }
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class, QuietReporter.class})
    public void approveStartingTimerFrame() throws InterruptedException, MalformedURLException {
        startTimer();

        Approvals.verify(BabystepsTimer.timerPane);
    }

}