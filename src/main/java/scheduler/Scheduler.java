package scheduler;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public final class Scheduler {
    private final ArrayList<ScheduledTask> jobs = new ArrayList<ScheduledTask>();
    private final Clock clock;
    final ExecutorService executorService;

    public Scheduler(Integer nThreads, Clock clock) {
        this.executorService  = Executors.newFixedThreadPool(nThreads);
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

    public void runPending() {
        jobs.stream()
            .filter(j -> j.shouldRun())
            .forEach(j -> {
                j.update();
                executorService.submit(j.getTask());
            });
    }

    public List<Future<Void>> runPendingCollect() {
        List<Future<Void>> results = jobs.stream()
            .filter(j -> j.shouldRun())
            .map(j -> {
                j.update();
                return executorService.submit(j.getTask());
            })
            .collect(Collectors.toList());

        return results;
    }

    public List<Future<Void>> runPendingCollectBlocking() throws InterruptedException{
        List<ScheduleCallable> tasks = jobs.stream()
            .filter(j -> j.shouldRun())
            .map(e -> {
                e.update();
                return e.getTask();
            })
            .collect(Collectors.toList());
        
        List<Future<Void>> futures = executorService.invokeAll(tasks);
        return futures;
    }

    public ArrayList<ScheduledTask> getJobs() {
        return jobs;
    }

    public void schedule(ScheduledTask task) {
        jobs.add(task);
    } 

    public void removeJob(ScheduledTask task) {
        jobs.remove(task);
    }

    public void removeJob(String name) {
        jobs.removeIf(j -> j.getName().equals(name));   
    }
}