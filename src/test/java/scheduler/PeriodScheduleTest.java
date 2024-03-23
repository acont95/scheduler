package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class PeriodScheduleTest {
    @Test
    public void shouldRunInitEqual()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2000-01-31T00:00:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).initialDelay(Period.ofDays(30))
        .build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitLater()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2000-02-15T00:00:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).initialDelay(Period.ofDays(30))
        .build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldntRunInitBefore()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2000-01-15T00:30:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).initialDelay(Period.ofDays(30))
        .build();

        assertFalse(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunEqual()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2001-01-02T00:00:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunLater()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2001-01-15T12:00:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunBefore()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2001-01-05T00:30:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(15)
        ).build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunTruncatedEquals()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2001-01-02T00:00:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).truncateTo(ChronoUnit.DAYS)
        .build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunTruncatedBefore()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2001-01-01T20:30:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).truncateTo(ChronoUnit.DAYS)
        .build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunTruncatedLater()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2001-01-04T05:30:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).truncateTo(ChronoUnit.DAYS)
        .build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunTruncatedInitialEqual()
    {
        Instant scheduledTime = Instant.parse("2001-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2001-01-11T00:00:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).truncateTo(ChronoUnit.DAYS)
        .initialDelay(Period.ofDays(10))
        .build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunTruncatedInitialLater()
    {
        Instant scheduledTime = Instant.parse("2001-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2001-01-30T00:30:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).truncateTo(ChronoUnit.DAYS)
        .initialDelay(Period.ofDays(10))
        .build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldntRunAlignedInitialBefore()
    {
        Instant scheduledTime = Instant.parse("2001-01-01T12:00:00.00Z");
        Instant now = Instant.parse("2001-01-10T20:30:00.00Z");

        PeriodSchedule sched = new PeriodSchedule.Builder(
            Period.ofDays(1)
        ).truncateTo(ChronoUnit.DAYS)
        .initialDelay(Period.ofDays(10))
        .build();

        assertFalse(sched.shouldRunInit(now, scheduledTime));
    }
}
