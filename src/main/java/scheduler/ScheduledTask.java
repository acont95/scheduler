package scheduler;

import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ScheduledTask {
    void setScheduledTime(Instant scheduledTime);
    Boolean shouldRun(Instant now);
    void setLastRun(Future<Void> callableStatus, Instant lastRun);
    Callable<Void> getTask();
    String getName();
    Future<Void> getStatus();
}
