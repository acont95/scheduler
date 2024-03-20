package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class MonthScheduleTest {
    @Test
    public void shouldntRunInitDifferentMonth()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-06-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        MonthSchedule sched = new MonthSchedule.Builder(
            Month.JANUARY
        ).build();

        assertFalse(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitSameMonth()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        MonthSchedule sched = new MonthSchedule.Builder(
            Month.JANUARY
        ).build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitSameMonthSameScheduledDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        MonthSchedule sched = new MonthSchedule.Builder(
            Month.JANUARY
        ).build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }


    @Test
    public void shouldRunDifferentLastRunMonth()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-15T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        MonthSchedule sched = new MonthSchedule.Builder(
            Month.JANUARY
        ).build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunSameLastRunMonth()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2000-01-15T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        MonthSchedule sched = new MonthSchedule.Builder(
            Month.JANUARY
        ).build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunDifferentLastRunMonth()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2000-02-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        MonthSchedule sched = new MonthSchedule.Builder(
            Month.JANUARY
        ).build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }
}
