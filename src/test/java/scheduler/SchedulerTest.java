package scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

public class SchedulerTest {
    @Test
    public void runPendingBlockingShouldRunAll() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPendingBlocking(clock.instant());
        assertTrue(results.size() == 1);
        assertTrue(results.get(0).isDone());

        Instant newTime = Instant.parse("2002-01-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPendingBlocking(newTime);
        assertTrue(newResults.size() == 1);
        assertTrue(newResults.get(0).isDone());
    }

    @Test
    public void runPendingBlockingShouldRunFirst() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPendingBlocking(clock.instant());
        assertTrue(results.size() == 1);
        assertTrue(results.get(0).isDone());

        Instant newTime = Instant.parse("2001-12-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPendingBlocking(newTime);
        assertTrue(newResults.size() == 0);
    }

    @Test
    public void runPendingBlockingShouldRunSecond() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPendingBlocking(clock.instant());
        assertTrue(results.size() == 0);

        Instant newTime = Instant.parse("2001-01-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPendingBlocking(newTime);
        assertTrue(newResults.size() == 1);
        assertTrue(newResults.get(0).isDone());
    }

    @Test
    public void runPendingBlockingShouldRunNone() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPendingBlocking(clock.instant());
        assertTrue(results.size() == 0);

        Instant newTime = Instant.parse("2001-12-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPendingBlocking(newTime);
        assertTrue(newResults.size() == 0);
    }

    @Test
    public void runPendingShouldRunAll() throws InterruptedException, ExecutionException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPending(clock.instant());
        assertTrue(results.size() == 1);
        results.get(0).get();
        assertTrue(results.get(0).isDone());

        Instant newTime = Instant.parse("2002-01-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPendingBlocking(newTime);
        assertTrue(newResults.size() == 1);
        newResults.get(0).get();
        assertTrue(newResults.get(0).isDone());
    }

    @Test
    public void runPendingShouldRunFirst() throws InterruptedException, ExecutionException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPending(clock.instant());
        assertTrue(results.size() == 1);
        results.get(0).get();
        assertTrue(results.get(0).isDone());

        Instant newTime = Instant.parse("2001-12-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPending(newTime);
        assertTrue(newResults.size() == 0);
    }

    @Test
    public void runPendingShouldRunSecond() throws InterruptedException, ExecutionException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPending(clock.instant());
        assertTrue(results.size() == 0);

        Instant newTime = Instant.parse("2001-01-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPending(newTime);
        assertTrue(newResults.size() == 1);
        newResults.get(0).get();
        assertTrue(newResults.get(0).isDone());
    }

    @Test
    public void runPendingShouldRunNone() throws InterruptedException, ExecutionException {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        List<Future<Void>> results = scheduler.runPending(clock.instant());
        assertTrue(results.size() == 0);

        Instant newTime = Instant.parse("2001-12-01T00:00:00.00Z");
        List<Future<Void>> newResults = scheduler.runPending(newTime);
        assertTrue(newResults.size() == 0);
    }

    @Test
    public void removeJobByNameValid() {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        assertTrue(scheduler.getJobs().size() == 1);
        assertTrue(scheduler.getJobs().get(0).getName() == "TEST_TASK");

        scheduler.removeJob("TEST_TASK");
        assertTrue(scheduler.getJobs().size() == 0);
    }

    @Test
    public void removeJobByNameInvalid() {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        assertTrue(scheduler.getJobs().size() == 1);
        assertTrue(scheduler.getJobs().get(0).getName() == "TEST_TASK");

        scheduler.removeJob("NOT_THE_NAME");
        assertTrue(scheduler.getJobs().size() == 1);
        assertTrue(scheduler.getJobs().get(0).getName() == "TEST_TASK");
    }

    @Test
    public void removeJobByReferenceValid() {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        assertTrue(scheduler.getJobs().size() == 1);
        assertTrue(scheduler.getJobs().get(0).getName() == "TEST_TASK");

        scheduler.removeJob(task);
        assertTrue(scheduler.getJobs().size() == 0);
    }

    @Test
    public void removeJobByReferenceInvalid() {
        Scheduler scheduler = new Scheduler();
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        ScheduleCallable testCallable = new ScheduleCallable(clock) {
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

        ScheduledTask unScheduledTask = new SingleScheduledTask(
            "TEST_TASK_UNSCHEDULED", 
            testCallable, 
            testSchedule,
            true
        );

        task.setScheduledTime(scheduledTime);

        scheduler.schedule(task, scheduledTime);

        assertTrue(scheduler.getJobs().size() == 1);
        assertTrue(scheduler.getJobs().get(0).getName() == "TEST_TASK");

        scheduler.removeJob(unScheduledTask);
        assertTrue(scheduler.getJobs().size() == 1);
        assertTrue(scheduler.getJobs().get(0).getName() == "TEST_TASK");
    }
}
