package net.davidtanzer.babysteps;

/**
 * Abstraction over (sub domain) scheduling a repeated task.
 */
public class RepeatingTaskThread extends Thread {

    private final RepeatingTask task;
    private final long pauseBetweenTasks;

    public RepeatingTaskThread(RepeatingTask task, long pauseBetweenTasks) {
        this.task = task;
        this.pauseBetweenTasks = pauseBetweenTasks;
    }

    @Override
    public void run() {
        while (task.isRunning()) {
            task.tick();
            sleep();
        }
    }

    private void sleep() {
        try {
            sleep(pauseBetweenTasks);
        } catch (InterruptedException e) {
            //We don't really care about this one...
        }
    }
}
