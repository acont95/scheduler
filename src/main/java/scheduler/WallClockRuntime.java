package scheduler;

import java.time.Clock;

public final class WallClockRuntime implements SchedulerRuntime{
    private final Clock clock;
    private final long sleepMillis;
    private volatile boolean run = false;

    public WallClockRuntime(Clock clock, long sleepMillis) {
        this.clock = clock;
        this.sleepMillis=sleepMillis;
    }   

    public WallClockRuntime(Clock clock) {
        this.clock = clock;
        this.sleepMillis=0;
    }   

    public void start(Scheduler scheduler) throws SchedulerRuntimeExecption {
        run = true;
        try {
            while (run) {
                if (sleepMillis != 0) {
                    Thread.sleep(sleepMillis);
                }
                scheduler.runPending(clock.instant());
            }            
        } catch (InterruptedException e) {
            throw new SchedulerRuntimeExecption(e.getMessage());
        }
    }

    public void stop() {
        run = false;
    }
}
