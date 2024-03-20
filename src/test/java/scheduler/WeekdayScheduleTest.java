package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class WeekdayScheduleTest {
    @Test
    public void shouldntRunInitWeekend()
    {
        Instant scheduledTime = Instant.parse("2010-01-02T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2010-01-03T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldntRunInitWeekendScheduledWeekday()
    {
        Instant scheduledTime = Instant.parse("2010-01-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2010-01-03T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitSameWeekday()
    {
        Instant scheduledTime = Instant.parse("2009-12-01T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2010-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }

    @Test
    public void shouldRunInitSameWeekdaySameScheduledDay()
    {
        Instant scheduledTime = Instant.parse("2010-01-04T00:00:00.00Z");
        Clock clock = Clock.fixed(Instant.parse("2010-01-04T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertTrue(sched.shouldRunInit(clock, scheduledTime));
    }


    @Test
    public void shouldRunDifferentLastRunWeekday()
    {
        Instant scheduledTime = Instant.parse("2010-01-04T00:00:00.00Z");
        Instant lastRun = Instant.parse("2010-01-04T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2010-01-05T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertTrue(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunSameLastRunWeekday()
    {
        Instant scheduledTime = Instant.parse("2010-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2010-01-04T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2010-01-04T12:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunDifferentLastRunMonth()
    {
        Instant scheduledTime = Instant.parse("2010-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-04T00:00:00.00Z");

        Clock clock = Clock.fixed(Instant.parse("2010-01-09T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRun(clock, lastRun, scheduledTime));
    }
}
