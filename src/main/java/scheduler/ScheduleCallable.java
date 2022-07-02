package scheduler;

import java.time.Clock;
import java.util.concurrent.Callable;

public abstract class ScheduleCallable implements Callable<Void>{
    volatile public Clock clock;

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}