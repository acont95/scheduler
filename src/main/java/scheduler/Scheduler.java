package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

interface ScheduledTask{
    Boolean shouldRun();
    Future<Void> run();
    void update();
    ScheduleCallable getTask();
}

class SingleScheduledTask implements ScheduledTask{
    final String name;
    final ScheduleCallable task;
    final ScheduleDefine sched;
    final Scheduler scheduler;
    final Instant scheduledTime;
    private Future<Void> callableStatus = null;
    Instant lastRun = null;

    public SingleScheduledTask(String name, ScheduleCallable task, ScheduleDefine sched, Scheduler scheduler) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.scheduler = scheduler;
        this.scheduledTime = Instant.now(scheduler.getClock());
    }

    public Boolean shouldRun() {
        if (! sched.isSynchronous()) {
            return sched.shouldRun(scheduler.getClock(), lastRun, scheduledTime);
        } else {
            if (callableStatus == null || callableStatus.isDone()) {
                return sched.shouldRun(scheduler.getClock(), lastRun, scheduledTime);
            }
            else {
                return false;
            }
        }
    }

    public Future<Void> run() {
        lastRun = Instant.now(scheduler.getClock());
        task.setClock(scheduler.getClock());
        callableStatus = scheduler.executorService.submit(task);
        return callableStatus;
    }

    public void update() {
        lastRun = Instant.now(scheduler.getClock());
        task.setClock(scheduler.getClock());
    }

    public ScheduleCallable getTask() {
        return task;
    }
}

class MultiScheduledTask implements ScheduledTask{
    final String name;
    final ScheduleCallable task;
    final ArrayList<ScheduleDefine> sched;
    final Scheduler scheduler;
    final Instant scheduledTime;
    private Future<Void> callableStatus = null;
    Instant lastRun = null;

    public MultiScheduledTask(String name, ScheduleCallable task, ArrayList<ScheduleDefine> sched, Scheduler scheduler) {
        this.name = name;
        this.task = task;
        this.sched = sched;
        this.scheduler = scheduler;
        this.scheduledTime = Instant.now(scheduler.getClock());
    }

    public Boolean shouldRun() {
        if (sched.stream().anyMatch(val -> val.isSynchronous() == false)) {
            return sched.stream().allMatch(val -> val.shouldRun(scheduler.getClock(), lastRun, scheduledTime));
        } else {
            if (callableStatus == null || callableStatus.isDone()) {
                return sched.stream().allMatch(val -> val.shouldRun(scheduler.getClock(), lastRun, scheduledTime));
            }
            else {
                return false;
            }
        }
    }

    public Future<Void> run() {
        // lastRun = Instant.now(scheduler.getClock());
        // task.setClock(scheduler.getClock());
        update();
        callableStatus = scheduler.executorService.submit(task);
        return callableStatus;
    }

    public void update() {
        lastRun = Instant.now(scheduler.getClock());
        task.setClock(scheduler.getClock());
    }

    public ScheduleCallable getTask() {
        return task;
    }
}

public class Scheduler {
    private final ArrayList<ScheduledTask> jobs = new ArrayList<ScheduledTask>();
    private Clock clock;
    final ExecutorService executorService;

    public Scheduler(Integer nThreads) {
        this.executorService  = Executors.newFixedThreadPool(nThreads);
    }

    public void updateClock(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

    public void runPending() {
        jobs.stream()
            .filter(j -> j.shouldRun())
            .forEach(j -> j.run());
            
        // jobs.removeIf(j -> j.sched.shouldDelete(clock));   
    }

    public ArrayList<Future<Void>> runPendingCollect() {
        List<Future<Void>> results = jobs.stream()
            .filter(j -> j.shouldRun())
            .map(j -> j.run())
            .collect(Collectors.toList());

        // jobs.removeIf(j -> j.sched.shouldDelete(clock));   
        return new ArrayList<>(results);
    }

    public List<Future<Void>> runPendingCollectBlocking() {
        List<ScheduleCallable> tasks = jobs.stream()
            .filter(j -> j.shouldRun())
            .map(e -> {
                e.update();
                return e.getTask();
            })
            .collect(Collectors.toList());
        
        try {
            List<Future<Void>> futures = executorService.invokeAll(tasks);
            return futures;
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
            return null;
        }

    }

    public ArrayList<ScheduledTask> getJobs() {
        return jobs;
    }

    public void schedule(String name, ScheduleCallable task, ScheduleDefine sched) {
        addJob(new SingleScheduledTask(name, task, sched, this));
    } 

    public void schedule(String name, ScheduleCallable task, ArrayList<ScheduleDefine> schedules) {
        addJob(new MultiScheduledTask(name, task, schedules, this));
    }

    void addJob(ScheduledTask job) {
        jobs.add(job);
    }

    public void start(SchedulerRuntime runtime) {
        runtime.run(this);
    }
}