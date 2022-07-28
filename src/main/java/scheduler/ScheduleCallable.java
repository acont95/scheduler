package scheduler;

import java.time.Clock;
import java.util.concurrent.Callable;

public abstract class ScheduleCallable implements Callable<Void>{
    protected final Clock clock;

    public ScheduleCallable(Clock clock) {
        this.clock = clock;
    }
}