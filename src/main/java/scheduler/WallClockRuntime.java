package scheduler;

import java.time.Clock;

public final class WallClockRuntime implements SchedulerRuntime{
    private final Clock clock;
    private final long sleepMillis;
    private volatile boolean run = false;

    public WallClockRuntime() {
        clock = Clock.systemUTC();
        sleepMillis=10;
    }   

    public WallClockRuntime(long sleepMillis) {
        clock = Clock.systemUTC();
        this.sleepMillis=sleepMillis;
    }   

    public void start(Scheduler scheduler){
        run = true;
        while (run) {
            try {
                if (sleepMillis != 0) {
                    Thread.sleep(sleepMillis);
                }
                scheduler.runPending();

            } catch (InterruptedException e) {
                System.err.format("IOException: %s%n", e);
            }
        }
    }

    public Clock getClock() {
        return clock;
    }

    public void stop() {
        run = false;
    }
}
