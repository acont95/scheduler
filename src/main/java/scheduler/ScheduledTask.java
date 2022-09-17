package scheduler;

import java.util.concurrent.Future;

public interface ScheduledTask {
    Boolean shouldRun();
    void setLastRun(Future<Void> callableStatus);
    ScheduleCallable getTask();
    String getName();
}
