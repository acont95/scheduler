package scheduler;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public final class SingleScheduledTask implements ScheduledTask{
    private final String name;
    private final ScheduleCallable task;
    private final ScheduleDefine sched;
    private Instant scheduledTime;
    private Future<Void> callableStatus = new CompletableFuture<>();
    private Instant lastRun;
    private boolean init = true;
    private final boolean synchronous;

    public SingleScheduledTask(String name, ScheduleCallable task, ScheduleDefine sched, boolean synchronous) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.synchronous = synchronous;
    }

    public SingleScheduledTask(String name, ScheduleCallable task, ScheduleDefine sched) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.synchronous = true;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Boolean shouldRun(Instant now) {
        if (init) {
            return sched.shouldRunInit(now, scheduledTime);
        } else {
            if (!synchronous) {
                return sched.shouldRun(now, lastRun, scheduledTime);
            } else {
                if (callableStatus.isDone()) {
                    return sched.shouldRun(now, lastRun, scheduledTime);
                }
                else {
                    return false;
                }
            }
        }
    }

    public void setLastRun(Future<Void> callableStatus, Instant lastRun) {
        init = false;
        this.lastRun = lastRun;
        this.callableStatus = callableStatus;
    }

    public ScheduleCallable getTask() {
        return task;
    }

    public String getName() {
        return name;
    }

    public Future<Void> getStatus() {
        return callableStatus;
    }
}
