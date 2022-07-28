package scheduler;

import java.time.Clock;

public interface SchedulerRuntime {
    void start(Scheduler scheduler);
    void stop();
    Clock getClock();
}
