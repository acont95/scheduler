package scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.threeten.extra.MutableClock;

public class WallClockRuntimeTest {
    @Test
    public void runPendingSchedulerShouldRun() throws SchedulerRuntimeExecption, 
        InterruptedException, 
        ExecutionException
    {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = MutableClock.of(
            Instant.parse("2001-01-01T00:00:00.00Z"), 
            ZoneId.ofOffset("UTC", ZoneOffset.UTC)
        );
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        ScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);

        scheduler.schedule(task, scheduledTime);

        SchedulerRuntime runtime = new WallClockRuntime(clock);
        List<Future<Void>> results = runtime.runPendingScheduler(scheduler);
        assertTrue(results.size() == 1);
        results.get(0).get();
        assertTrue(results.get(0).isDone());
    }

    @Test
    public void runPendingSchedulerShouldntRun() throws SchedulerRuntimeExecption, 
        InterruptedException, 
        ExecutionException
    {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = MutableClock.of(
            Instant.parse("2000-12-15T00:00:00.00Z"), 
            ZoneId.ofOffset("UTC", ZoneOffset.UTC)
        );
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        ScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);

        scheduler.schedule(task, scheduledTime);

        SchedulerRuntime runtime = new WallClockRuntime(clock);
        List<Future<Void>> results = runtime.runPendingScheduler(scheduler);
        assertTrue(results.size() == 0);
    }

    @Test
    public void runPendingSchedulerWalkClock() throws SchedulerRuntimeExecption, 
        InterruptedException, 
        ExecutionException
    {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        MutableClock clock = MutableClock.of(
            Instant.parse("2000-12-15T00:00:00.00Z"), 
            ZoneId.ofOffset("UTC", ZoneOffset.UTC)
        );
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        ScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);

        scheduler.schedule(task, scheduledTime);

        SchedulerRuntime runtime = new WallClockRuntime(clock);
        List<Future<Void>> results = runtime.runPendingScheduler(scheduler);
        assertTrue(results.size() == 0);

        clock.setInstant(Instant.parse("2001-01-01T00:00:00.00Z"));
        List<Future<Void>> newResults = runtime.runPendingScheduler(scheduler);
        assertTrue(newResults.size() == 1);
        newResults.get(0).get();
        assertTrue(newResults.get(0).isDone());
    }
}
