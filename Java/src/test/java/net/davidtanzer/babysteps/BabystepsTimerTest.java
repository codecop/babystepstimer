package net.davidtanzer.babysteps;

import org.approvaltests.Approvals;
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