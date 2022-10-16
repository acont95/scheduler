package scheduler;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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

    public List<Future<Void>> runPending() {
        return jobs.stream()
            .filter(j -> j.shouldRun())
            .map(j -> {
                Future<Void> result = executorService.submit(j.getTask());
                j.setLastRun(result);
                return result;
            })
            .toList();
    }

    public List<Future<Void>> runPendingBlocking() throws InterruptedException{

        List<ScheduledTask> tasks = jobs.stream()
            .filter(j -> j.shouldRun())
            .toList();

        List<ScheduleCallable> callables = tasks.stream()
            .map(t -> {return t.getTask();})  
            .toList();      

        List<Future<Void>> futures = executorService.invokeAll(callables);

        for (int i=0; i<tasks.size(); i++) {
            tasks.get(i).setLastRun(futures.get(i));
        }

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

    public void shutDown() {
        executorService.shutdown();
    }
}