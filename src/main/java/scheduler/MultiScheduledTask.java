package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

public final class MultiScheduledTask implements ScheduledTask{
    private final String name;
    private final ScheduleCallable task;
    private final List<ScheduleDefine> sched;
    private Instant scheduledTime;
    private Future<Void> callableStatus = null;
    private Instant lastRun;
    private boolean init = true;
    private final boolean synchronous;

    public MultiScheduledTask(String name, ScheduleCallable task, List<ScheduleDefine> sched, boolean synchronous) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.synchronous = synchronous;
    }

    public MultiScheduledTask(String name, ScheduleCallable task, List<ScheduleDefine> sched) {
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
            boolean shouldRun = sched.stream().allMatch(val -> val.shouldRunInit(clock, scheduledTime));
            if (shouldRun) {
                init = false;
                return shouldRun;
            } else {
                return shouldRun;
            }
        } else {
            if (!synchronous) {
                return sched.stream().allMatch(val -> val.shouldRun(clock, lastRun, scheduledTime));
            } else {
                if (callableStatus.isDone()) {
                    return sched.stream().allMatch(val -> val.shouldRun(clock, lastRun, scheduledTime));
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
