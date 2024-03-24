package scheduler;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.Future;

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

    public List<Future<Void>> runPendingScheduler(Scheduler scheduler) throws SchedulerRuntimeExecption {
        try {
            if (sleepMillis != 0) {
                Thread.sleep(sleepMillis);
            }
            return scheduler.runPending(clock.instant());
        } catch (InterruptedException e) {
            throw new SchedulerRuntimeExecption(e.getMessage());
        }
    }

    public void start(Scheduler scheduler) throws SchedulerRuntimeExecption {
        run = true;
        while (run) {
            runPendingScheduler(scheduler);
        }            
    }

    public void stop() {
        run = false;
    }
}
