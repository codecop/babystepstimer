package net.davidtanzer.babysteps;

import org.junit.Assert;

import java.util.function.Supplier;

public class RetryAssert {

    private static final Object MONITOR = new Object();

    public static void assertEquals(String expectedString, Supplier<String> actual, int millis) {
        synchronized (MONITOR) {
            String actualString = actual.get();
            if (!expectedString.equals(actualString)) {
                waitBeforeRetry(millis);
                System.out.println("trying again");
                actualString = actual.get();
            }
            Assert.assertEquals(expectedString, actualString);
        }
    }

    private static void waitBeforeRetry(int millis) {
        synchronized (MONITOR) {
            try {
                MONITOR.wait(millis);
            } catch (@SuppressWarnings("unused") InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
