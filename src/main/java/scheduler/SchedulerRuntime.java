package scheduler;

import java.util.List;
import java.util.concurrent.Future;

public interface SchedulerRuntime {
    List<Future<Void>> runPendingScheduler(Scheduler scheduler) throws SchedulerRuntimeExecption;
    void start(Scheduler scheduler) throws SchedulerRuntimeExecption;
    void stop();
}
