package scheduler;

public interface SchedulerRuntime {
    void start(Scheduler scheduler) throws SchedulerRuntimeExecption;
    void stop();
}
