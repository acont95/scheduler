package scheduler;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public final class MultiScheduledTask implements ScheduledTask{
    private final String name;
    private final Callable<Void> task;
    private final List<ScheduleDefine> sched;
    private Instant scheduledTime;
    private Future<Void> callableStatus = new CompletableFuture<>();
    private Instant lastRun;
    private boolean init = true;
    private final boolean synchronous;

    public MultiScheduledTask(String name, Callable<Void> task, List<ScheduleDefine> sched, boolean synchronous) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.synchronous = synchronous;
    }

    public MultiScheduledTask(String name, Callable<Void> task, List<ScheduleDefine> sched) {
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
            return sched.stream().allMatch(val -> val.shouldRunInit(now, scheduledTime));
        } else {
            if (!synchronous) {
                return sched.stream().allMatch(val -> val.shouldRun(now, lastRun, scheduledTime));
            } else {
                if (callableStatus.isDone()) {
                    return sched.stream().allMatch(val -> val.shouldRun(now, lastRun, scheduledTime));
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

    public Callable<Void> getTask() {
        return task;
    }

    public String getName() {
        return name;
    }

    public Future<Void> getStatus() {
        return callableStatus;
    }
}
