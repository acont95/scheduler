package scheduler;

public interface ScheduledTask {
    Boolean shouldRun();
    void update();
    ScheduleCallable getTask();
    String getName();
}
