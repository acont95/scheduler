package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;

public class DayOfWeekScheduleTest {
    @Test
    public void shouldntRunInitDifferentWeekDay()
    {
        Instant scheduledTime = Instant.parse("2020-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2020-01-08T12:00:00.00Z");

        DayOfWeekSchedule sched = new DayOfWeekSchedule.Builder(
            DayOfWeek.of(1)
        ).build();

        assertFalse(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitSameWeekDay()
    {
        Instant scheduledTime = Instant.parse("2020-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2020-01-06T12:00:00.00Z");

        DayOfWeekSchedule sched = new DayOfWeekSchedule.Builder(
            DayOfWeek.of(1)
        ).build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitSameMonthDaySameScheduledDay()
    {
        Instant scheduledTime = Instant.parse("2020-01-06T12:00:00.00Z");
        Instant now = Instant.parse("2020-01-06T12:00:00.00Z");

        DayOfWeekSchedule sched = new DayOfWeekSchedule.Builder(
            DayOfWeek.of(1)
        ).build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }


    @Test
    public void shouldRunDifferentLastRunWeekDay()
    {
        Instant scheduledTime = Instant.parse("2020-01-01T12:00:00.00Z");
        Instant lastRun = Instant.parse("2020-01-06T12:00:00.00Z");
        Instant now = Instant.parse("2020-01-13T12:00:00.00Z");

        DayOfWeekSchedule sched = new DayOfWeekSchedule.Builder(
            DayOfWeek.of(1)
        ).build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunSameLastRunWeekDay()
    {
        Instant scheduledTime = Instant.parse("2020-01-01T12:00:00.00Z");
        Instant lastRun = Instant.parse("2020-01-06T12:00:00.00Z");
        Instant now = Instant.parse("2020-01-06T12:00:00.00Z");

        DayOfWeekSchedule sched = new DayOfWeekSchedule.Builder(
            DayOfWeek.of(1)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunDifferentLastRunWeekDay()
    {
        Instant scheduledTime = Instant.parse("2020-01-01T12:00:00.00Z");
        Instant lastRun = Instant.parse("2020-01-06T12:00:00.00Z");
        Instant now = Instant.parse("2020-01-10T12:00:00.00Z");

        DayOfWeekSchedule sched = new DayOfWeekSchedule.Builder(
            DayOfWeek.of(1)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }
}
