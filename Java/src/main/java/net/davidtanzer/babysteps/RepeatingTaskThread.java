package net.davidtanzer.babysteps;

class RepeatingTaskThread extends Thread {

    private final RepeatingTask task;
    private final long pauseBetweenRuns;

    public RepeatingTaskThread(final RepeatingTask task, final long pauseBetweenRuns) {
        this.task = task;
        this.pauseBetweenRuns = pauseBetweenRuns;
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
            sleep(pauseBetweenRuns);
        } catch (InterruptedException e) {
            //We don't really care about this one...
        }
    }
}
