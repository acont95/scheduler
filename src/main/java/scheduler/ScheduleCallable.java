package scheduler;

import java.time.Clock;
import java.util.concurrent.Callable;

public abstract class ScheduleCallable implements Callable<Void>{
    private Clock clock;
    public ScheduleCallable(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }
}