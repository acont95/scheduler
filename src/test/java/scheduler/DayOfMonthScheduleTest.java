package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.threeten.extra.DayOfMonth;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DayOfMonthScheduleTest {
    @Test
    public void shouldntRunInitDifferentMonthDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-12T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        DayOfMonthSchedule sched = new DayOfMonthSchedule.Builder(
            DayOfMonth.of(1)
        ).build();

        assertFalse(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitSameMonthDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-02-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        DayOfMonthSchedule sched = new DayOfMonthSchedule.Builder(
            DayOfMonth.of(1)
        ).build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitSameMonthDaySameScheduledDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        DayOfMonthSchedule sched = new DayOfMonthSchedule.Builder(
            DayOfMonth.of(1)
        ).build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }


    @Test
    public void shouldRunDifferentLastRunMonthDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-12T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2000-02-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        DayOfMonthSchedule sched = new DayOfMonthSchedule.Builder(
            DayOfMonth.of(1)
        ).build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunSameLastRunMonthDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-02-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2000-02-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        DayOfMonthSchedule sched = new DayOfMonthSchedule.Builder(
            DayOfMonth.of(1)
        ).build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunDifferentLastRunMonthDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-02-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2000-02-12T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        DayOfMonthSchedule sched = new DayOfMonthSchedule.Builder(
            DayOfMonth.of(1)
        ).build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }
}
