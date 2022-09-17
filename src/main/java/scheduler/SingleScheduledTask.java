package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.Future;

public final class SingleScheduledTask implements ScheduledTask{
    private final String name;
    private final ScheduleCallable task;
    private final ScheduleDefine sched;
    private final Instant scheduledTime;
    private Future<Void> callableStatus;
    private Instant lastRun;
    private final Clock clock;
    private boolean init = true;
    private final boolean synchronous;

    public SingleScheduledTask(String name, ScheduleCallable task, ScheduleDefine sched, Clock clock, boolean synchronous) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.clock = clock;
        this.scheduledTime = Instant.now(clock);
        this.synchronous = synchronous;
    }

    public SingleScheduledTask(String name, ScheduleCallable task, ScheduleDefine sched, Clock clock) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.clock = clock;
        this.scheduledTime = Instant.now(clock);
        this.synchronous = true;
    }

    public Boolean shouldRun() {

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

    public void setLastRun(Future<Void> callableStatus) {
        lastRun = Instant.now(clock);
        this.callableStatus = callableStatus;
    }

    public ScheduleCallable getTask() {
        return task;
    }

    public String getName() {
        return name;
    }
}
