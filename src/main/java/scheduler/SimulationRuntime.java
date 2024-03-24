package scheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

import org.threeten.extra.MutableClock;

public final class SimulationRuntime implements SchedulerRuntime{
    private final Instant start;
    private final Instant end;
    private final Duration step;
    private final Duration stepPause;
    private final MutableClock clock;
    private volatile boolean run = false;

    public SimulationRuntime(MutableClock clock, Instant start, Instant end, Duration step, Duration stepPause) {
        this.clock = clock;
        clock.setInstant(start);
        this.start = start;
        this.end = end;
        this.step = step;
        this.stepPause = stepPause;
    }   

    public List<Future<Void>> runPendingScheduler(Scheduler scheduler) throws SchedulerRuntimeExecption{
        long sleepMillis = stepPause.toMillis();
        
        try {
            if (sleepMillis != 0) {
                Thread.sleep(sleepMillis);
            }
            List<Future<Void>> results =  scheduler.runPendingBlocking(clock.instant());
            return results;
        } catch (InterruptedException e) {
            throw new SchedulerRuntimeExecption(e.getMessage());
        }
    }

    public void start(Scheduler scheduler) throws SchedulerRuntimeExecption {
        run = true;
        while (run) {
            runPendingScheduler(scheduler);
            progress();
        }            
    }

    public void stop() {
        run = false;
    }

    protected void progress() {
        clock.add(step);
        if (clock.instant().isAfter(end)) {
            run=false;
        }
    }
}
