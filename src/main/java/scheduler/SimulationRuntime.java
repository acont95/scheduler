package scheduler;

import java.time.Clock;
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

    public void start(Scheduler scheduler){
        long sleepMillis;
        if (stepPause.equals(Duration.ZERO)) {
            sleepMillis = 0;
        } else {
            sleepMillis = stepPause.toMillis();
        }

        run = true;
        
        while (clock.instant().isBefore(end) && run) {
            try {
                if (sleepMillis != 0) {
                    Thread.sleep(sleepMillis);
                }
                scheduler.runPendingBlocking(clock);
                clock.add(step);

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
