package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalTime;

public class LocalTimeScheduleTest {
    @Test
    public void shouldntRunInitEalierLocalTime()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2000-01-01T06:00:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertFalse(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitEqualLocalTime()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2000-01-01T12:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitLaterLocalTime()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2000-01-01T20:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunSameLocalTimeNewDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T12:30:00.00Z");
        Instant now = Instant.parse("2000-01-02T12:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunLaterLocalTimeNewDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T12:30:00.00Z");
        Instant now = Instant.parse("2000-01-02T20:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunEarlierLocalTimeNewDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T12:30:00.00Z");
        Instant now = Instant.parse("2000-01-02T06:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunSameLocalTimeSameDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T12:30:00.00Z");
        Instant now = Instant.parse("2000-01-01T12:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunLaterLocalTimeSameDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T12:30:00.00Z");
        Instant now = Instant.parse("2000-01-01T20:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunEarlierLocalTimeSameDay()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-01T12:30:00.00Z");
        Instant now = Instant.parse("2000-01-01T06:30:00.00Z");

        LocalTimeSchedule sched = new LocalTimeSchedule.Builder(
            LocalTime.of(12, 30)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }
}
