package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.Future;

public interface ScheduledTask {
    void setScheduledTime(Instant scheduledTime);
    Boolean shouldRun(Clock clock);
    void setLastRun(Future<Void> callableStatus, Clock clock);
    ScheduleCallable getTask();
    String getName();
    void setClock(Clock clock);
}
