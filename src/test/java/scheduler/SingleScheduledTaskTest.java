package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

public class SingleScheduledTaskTest {
    @Test
    public void shouldRunInit() {
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
            @Override
            public Void call() {
                System.out.println(Instant.now(getClock()));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        SingleScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);
        assertTrue(task.shouldRun(clock));
    }

    @Test
    public void shouldntRunInit() {
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-30T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
            @Override
            public Void call() {
                System.out.println(Instant.now(getClock()));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        SingleScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);
        assertFalse(task.shouldRun(clock));
    }

    @Test
    public void shouldRunComplete() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
            @Override
            public Void call() {
                System.out.println(Instant.now(getClock()));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        SingleScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        testResult.complete(null);
        task.setLastRun(testResult, lastRun);
        assertTrue(task.shouldRun(clock));
    }

    @Test
    public void shouldntRunComplete() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
            @Override
            public Void call() {
                System.out.println(Instant.now(getClock()));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        SingleScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        testResult.complete(null);
        task.setLastRun(testResult, lastRun);
        assertFalse(task.shouldRun(clock));
    }

    @Test
    public void shouldntRunIncompleteSynch() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
            @Override
            public Void call() {
                System.out.println(Instant.now(getClock()));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        SingleScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            true
        );
        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        task.setLastRun(testResult, lastRun);
        assertFalse(task.shouldRun(clock));
    }

    @Test
    public void shouldRunIncompleteAsynch() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
            @Override
            public Void call() {
                System.out.println(Instant.now(getClock()));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();

        SingleScheduledTask task = new SingleScheduledTask(
            "TEST_TASK", 
            testCallable, 
            testSchedule,
            false
        );
        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        task.setLastRun(testResult, lastRun);
        assertTrue(task.shouldRun(clock));
    }
}
