package scheduler;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import org.threeten.extra.MutableClock;

public final class SimulationRuntime implements SchedulerRuntime{
    private final Instant end;
    private final Duration step;
    private final Duration stepPause;
    private final MutableClock clock;
    private volatile boolean run = false;

    public SimulationRuntime(Instant start, Instant end, Duration step, Duration stepPause) {
        this.end = end;
        this.step = step;
        this.stepPause = stepPause;
        clock = MutableClock.of(start, ZoneOffset.UTC);
    }   

    public void start(Scheduler scheduler) throws SchedulerRuntimeExecption {
        long sleepMillis = stepPause.toMillis();
        run = true;
        
        try {
            while (clock.instant().isBefore(end) && run) {
                if (sleepMillis != 0) {
                    Thread.sleep(sleepMillis);
                }
                scheduler.runPendingBlocking(clock.instant());
                clock.add(step);
            }            
        } catch (InterruptedException e) {
            throw new SchedulerRuntimeExecption(e.getMessage());
        }
    }

    public void stop() {
        run = false;
    }
}
