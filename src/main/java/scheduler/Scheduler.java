package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public final class Scheduler {
    private final ArrayList<ScheduledTask> jobs = new ArrayList<ScheduledTask>();
    final ExecutorService executorService;

    public Scheduler(Integer nThreads) {
        this.executorService  = Executors.newFixedThreadPool(nThreads);
    }

    public void setClocks(Clock clock) {
        jobs.stream()
            .forEach(j -> {
                j.setClock(clock);
            });
    }

    public List<Future<Void>> runPending(Clock clock) {
        return jobs.stream()
            .filter(j -> j.shouldRun(clock))
            .map(j -> {
                Future<Void> result = executorService.submit(j.getTask());
                j.setLastRun(result, clock);
                return result;
            })
            .toList();
    }

    public List<Future<Void>> runPendingBlocking(Clock clock) throws InterruptedException{

        List<ScheduledTask> tasks = jobs.stream()
            .filter(j -> j.shouldRun(clock))
            .toList();

        List<ScheduleCallable> callables = tasks.stream()
            .map(t -> {return t.getTask();})  
            .toList();      

        List<Future<Void>> futures = executorService.invokeAll(callables);

        for (int i=0; i<tasks.size(); i++) {
            tasks.get(i).setLastRun(futures.get(i), clock);
        }

        return futures;
    }

    public ArrayList<ScheduledTask> getJobs() {
        return jobs;
    }

    public void schedule(ScheduledTask task, Instant scheduledTime) {
        task.setScheduledTime(scheduledTime);
        jobs.add(task);
    } 

    public void removeJob(ScheduledTask task) {
        jobs.remove(task);
    }

    public void removeJob(String name) {
        jobs.removeIf(j -> j.getName().equals(name));   
    }

    public void shutDown() {
        executorService.shutdown();
    }
}