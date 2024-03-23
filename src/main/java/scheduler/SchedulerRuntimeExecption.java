package scheduler;

public class SchedulerRuntimeExecption extends Exception {
    public SchedulerRuntimeExecption(String message) {
        super(message);
    }    

    public SchedulerRuntimeExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
