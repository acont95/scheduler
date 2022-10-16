package scheduler;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import org.threeten.extra.MutableClock;

public final class SimulationRuntime implements SchedulerRuntime{
    private final MutableClock clock;
    private final SimulationConfig simulationConfig;
    private volatile boolean run = false;

    public SimulationRuntime(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
        clock = MutableClock.of(simulationConfig.start().toInstant(), simulationConfig.start().getZone());
    }   

    public void start(Scheduler scheduler){
        long sleepMillis;
        if (simulationConfig.stepPause().equals(Duration.ZERO)) {
            sleepMillis = 0;
        } else {
            sleepMillis = simulationConfig.stepPause().toMillis();
        }

        run = true;
        Instant endInstant = simulationConfig.end().toInstant();
        
        while (clock.instant().isBefore(endInstant) && run) {
            try {
                if (sleepMillis != 0) {
                    Thread.sleep(sleepMillis);
                }
                scheduler.runPendingBlocking();
                clock.add(simulationConfig.step());

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
