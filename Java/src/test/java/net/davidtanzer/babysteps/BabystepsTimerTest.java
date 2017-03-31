package net.davidtanzer.babysteps;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.*;
import org.approvaltests.reporters.macosx.KDiff3Reporter;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public class BabystepsTimerTest {

    /*
     - new
     - start
        - stop
        - reset
     - quite
     - time passed

     */
    @Test
    @UseReporter(ImageWebReporter.class)
    public void approveInitialFrame() throws InterruptedException {
        BabystepsTimer babystepsTimer = new BabystepsTimer();
        babystepsTimer.main(new String[0]);
        JFrame timerFrame = BabystepsTimer.timerFrame;
        Thread.sleep(3000);

        Approvals.verify(BabystepsTimer.timerPane);

        timerFrame.setVisible(false);
        timerFrame.dispose();
    }

    @Test
    @Ignore("Graphics changes when run several time in same JVM, can not fix the component tree.")
    @UseReporter({KDiff3Reporter.class, ClipboardReporter.class, JunitReporter.class, QuietReporter.class})
    public void approveInitialFrameAsString() throws InterruptedException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        BabystepsTimer babystepsTimer = new BabystepsTimer();
        babystepsTimer.main(new String[0]);
        JFrame timerFrame = BabystepsTimer.timerFrame;
        Thread.sleep(3000);

        Approvals.verify(new JavaBeanApprovalWriter(timerFrame));

        timerFrame.setVisible(false);
        timerFrame.dispose();
    }


}