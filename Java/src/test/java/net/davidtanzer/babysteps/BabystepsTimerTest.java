package net.davidtanzer.babysteps;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.*;
import org.approvaltests.reporters.macosx.KDiff3Reporter;
import org.approvaltests.writers.JavaBeanApprovalWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public class BabystepsTimerTest {
    private JFrame timerFrame;

    @Before
    public void createTimer() throws InterruptedException {
        BabystepsTimer.main(new String[0]);
        timerFrame = BabystepsTimer.timerFrame;
        Thread.sleep(3000);
    }

    private void startTimer() throws InterruptedException {
        send("command://start");
        Thread.sleep(1500);
    }

    private void send(String desc) {
        SwingUtilities.invokeLater(() -> {
            HyperlinkEvent event = new HyperlinkEvent(BabystepsTimer.timerPane, HyperlinkEvent.EventType.ACTIVATED, null, desc);
            BabystepsTimer.timerPane.getHyperlinkListeners()[0].hyperlinkUpdate(event);
        });
    }

    @After
    public void closeTimer() throws InterruptedException {
        timerFrame.setVisible(false);
        timerFrame.dispose();
        BabystepsTimer.time = System::currentTimeMillis;
        Thread.sleep(1500);
    }

    @Test
    @UseReporter(ImageWebReporter.class)
    public void approveInitialFrame() {
        Approvals.verify(BabystepsTimer.timerPane);
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class})
    public void approveStartingTimerFrame() throws InterruptedException {
        startTimer();

        Approvals.verify(BabystepsTimer.timerPane);
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class, QuietReporter.class})
    public void approveStopTimerFrame() throws InterruptedException {
        startTimer();

        send("command://stop");

        Thread.sleep(1500);
        Approvals.verify(BabystepsTimer.timerPane);
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class, QuietReporter.class})
    public void approveResetTimerFrameIsGreen() throws InterruptedException {
        startTimer();

        send("command://reset");

        Thread.sleep(1500);
        Approvals.verify(BabystepsTimer.timerPane);
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class, QuietReporter.class})
    public void approveResetTimerFrameIsWhiteAfter5Sec() throws InterruptedException {
        startTimer();

        send("command://reset");
        Thread.sleep(5800);

        stopTime();
        Approvals.verify(BabystepsTimer.timerPane);
    }

    private void stopTime() {
        long t = BabystepsTimer.time.get();
        BabystepsTimer.time = () -> t;
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class, QuietReporter.class})
    public void approveTimerGetsRedWhenRuningOut() throws InterruptedException {
        startTimer();

        BabystepsTimer.time = () -> System.currentTimeMillis() + (60 + 58) * 1000;
        Thread.sleep(1000);

        Approvals.verify(BabystepsTimer.timerPane);
    }

    @Test
    @UseReporter({ImageWebReporter.class, ClipboardReporter.class, QuietReporter.class})
    public void approveTimerRunsOver() throws InterruptedException {
        startTimer();

        BabystepsTimer.time = () -> System.currentTimeMillis() + (60 + 58) * 1000;
        Thread.sleep(3500);

        stopTime();
        Approvals.verify(BabystepsTimer.timerPane);
    }

    @Test
    @Ignore("Graphics changes when run several time in same JVM, can not fix the component tree.")
    @UseReporter({KDiff3Reporter.class, ClipboardReporter.class, JunitReporter.class, QuietReporter.class})
    public void approveInitialFrameAsString() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Approvals.verify(new JavaBeanApprovalWriter(timerFrame));
    }

}