package scheduler;

import java.time.Clock;
import java.util.concurrent.Callable;

public abstract class ScheduleCallable implements Callable<Void>{
    protected Clock clock;
    protected void setClock(Clock clock) {
        this.clock = clock;
    }
}