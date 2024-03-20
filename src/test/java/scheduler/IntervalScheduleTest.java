package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class IntervalScheduleTest {
    @Test
    public void shouldRunInitEqual()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-01T01:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofSeconds(1)
        ).initialDelay(Duration.ofHours(1))
        .build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitLater()
    {
        Instant now = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-01T12:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofSeconds(1)
        ).initialDelay(Duration.ofHours(1))
        .build();

        assertTrue(sched.shouldRunInit(clock, now));
    }

    @Test
    public void shouldntRunInitBefore()
    {
        Instant now = Instant.parse("2000-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:30:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofSeconds(1)
        ).initialDelay(Duration.ofHours(1))
        .build();

        assertFalse(sched.shouldRunInit(clock, now));
    }

    @Test
    public void shouldRunEqual()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T01:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(1)
        ).build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunLater()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T12:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(1)
        ).build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunBefore()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T00:30:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(1)
        ).build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunAlignedEquals()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T02:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T04:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(4)
        ).alignedToEpoch()
        .build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunAlignedBefore()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T02:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T03:30:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(4)
        ).alignedToEpoch()
        .build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunAlignedLater()
    {
        Instant scheduledTime = Instant.parse("2000-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2001-01-01T02:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T05:30:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(4)
        ).alignedToEpoch()
        .build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldRunAlignedInitialEqual()
    {
        Instant scheduledTime = Instant.parse("2001-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T04:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(4)
        ).initialDelay(Duration.ofHours(4))
        .alignedToEpoch()
        .build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunAlignedInitialLater()
    {
        Instant scheduledTime = Instant.parse("2001-01-01T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T06:30:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(4)
        ).initialDelay(Duration.ofHours(4))
        .alignedToEpoch()
        .build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldntRunAlignedInitialBefore()
    {
        Instant scheduledTime = Instant.parse("2001-01-01T01:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2001-01-01T04:30:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        IntervalSchedule sched = new IntervalSchedule.Builder(
            Duration.ofHours(4)
        ).initialDelay(Duration.ofHours(4))
        .alignedToEpoch()
        .build();

        assertFalse(sched.shouldRunInit(clock, scheduledTime));
    }
}
