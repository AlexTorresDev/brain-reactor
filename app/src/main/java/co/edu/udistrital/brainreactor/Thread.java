package co.edu.udistrital.brainreactor;

public class Thread {

    static class ThreadJava extends java.lang.Thread {

        ThreadJava(Runnable target) {
            super(target);
        }

    }

    private ThreadJava thread;
    private boolean paused, stopped;
    private final Runnable target;

    public Thread(Runnable target) {
        this.target = target;
        this.stopped = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    private void init() {
        paused = false;
        stopped = false;
        thread = new ThreadJava(target);
    }

    public void start() {
        init();
        thread.start();
    }

    public void pause() {
        paused = true;
        stopped = false;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        stopped = true;
    }

    public void restart() {
        stop();
        init();
        start();
    }

    public static void sleep(long millis) throws InterruptedException {
        ThreadJava.sleep(millis);
    }
}
