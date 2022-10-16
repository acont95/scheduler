package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.Future;

public final class SingleScheduledTask implements ScheduledTask{
    private final String name;
    private final ScheduleCallable task;
    private final ScheduleDefine sched;
    private Instant scheduledTime;
    private Future<Void> callableStatus;
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

    public Boolean shouldRun(Clock clock) {

        if (init) {
            boolean shouldRun = sched.shouldRunInit(clock, scheduledTime);
            if (shouldRun) {
                init = false;
                return shouldRun;
            } else {
                return shouldRun;
            }
        } else {
            if (!synchronous) {
                return sched.shouldRun(clock, lastRun, scheduledTime);
            } else {
                if (callableStatus.isDone()) {
                    return sched.shouldRun(clock, lastRun, scheduledTime);
                }
                else {
                    return false;
                }
            }
        }
    }

    public void setLastRun(Future<Void> callableStatus, Clock clock) {
        lastRun = Instant.now(clock);
        this.callableStatus = callableStatus;
    }

    public ScheduleCallable getTask() {
        return task;
    }

    public String getName() {
        return name;
    }

    public void setClock(Clock clock) {
        task.setClock(clock);
    }
}
