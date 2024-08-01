package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.threeten.extra.DayOfMonth;

public class MultiScheduledTaskTest {
    @Test
    public void shouldRunInit() {
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );

        task.setScheduledTime(scheduledTime);
        assertTrue(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldntRunInit() {
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-30T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );

        task.setScheduledTime(scheduledTime);
        assertFalse(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldntRunInitOneShouldRun() {
        Instant scheduledTime = Instant.parse("2000-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-15T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );
        
        task.setScheduledTime(scheduledTime);
        assertFalse(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldRunComplete() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );

        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        testResult.complete(null);
        task.setLastRun(testResult, lastRun);
        assertTrue(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldntRunComplete() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-12-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );

        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        testResult.complete(null);
        task.setLastRun(testResult, lastRun);
        assertFalse(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldntRunCompleteOneShouldRun() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-15T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );
        
        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        testResult.complete(null);
        task.setLastRun(testResult, lastRun);
        assertFalse(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldntRunIncompleteSynch() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            true
        );

        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        task.setLastRun(testResult, lastRun);
        assertFalse(task.shouldRun(clock.instant()));
    }

    @Test
    public void shouldRunIncompleteAsynch() {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-15T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        Callable<Void> testCallable = new Callable<Void>() {
            @Override
            public Void call() {
                System.out.println(Instant.now(clock));
                return null;
            }
        };

        MonthSchedule testSchedule = new MonthSchedule.Builder(Month.JANUARY).build();
        DayOfMonthSchedule dayOfMonthSchedule = new DayOfMonthSchedule.Builder(DayOfMonth.of(1)).build();
        List<ScheduleDefine> scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);
        scheduleList.add(dayOfMonthSchedule);

        MultiScheduledTask task = new MultiScheduledTask(
            "TEST_TASK", 
            testCallable, 
            scheduleList,
            false
        );

        task.setScheduledTime(scheduledTime);
        CompletableFuture<Void> testResult = new CompletableFuture<>();
        task.setLastRun(testResult, lastRun);
        assertTrue(task.shouldRun(clock.instant()));
    }
}
