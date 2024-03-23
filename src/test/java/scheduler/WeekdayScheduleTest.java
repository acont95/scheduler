package scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Instant;

public class WeekdayScheduleTest {
    @Test
    public void shouldntRunInitWeekend()
    {
        Instant scheduledTime = Instant.parse("2010-01-02T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-03T00:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldntRunInitWeekendScheduledWeekday()
    {
        Instant scheduledTime = Instant.parse("2010-01-01T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-03T00:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitSameWeekday()
    {
        Instant scheduledTime = Instant.parse("2009-12-01T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-01T00:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }

    @Test
    public void shouldRunInitSameWeekdaySameScheduledDay()
    {
        Instant scheduledTime = Instant.parse("2010-01-04T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-04T00:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertTrue(sched.shouldRunInit(now, scheduledTime));
    }


    @Test
    public void shouldRunDifferentLastRunWeekday()
    {
        Instant scheduledTime = Instant.parse("2010-01-04T00:00:00.00Z");
        Instant lastRun = Instant.parse("2010-01-04T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-05T00:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertTrue(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunSameLastRunWeekday()
    {
        Instant scheduledTime = Instant.parse("2010-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2010-01-04T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-04T12:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }

    @Test
    public void shouldntRunDifferentLastRunMonth()
    {
        Instant scheduledTime = Instant.parse("2010-01-01T00:00:00.00Z");
        Instant lastRun = Instant.parse("2000-01-04T00:00:00.00Z");
        Instant now = Instant.parse("2010-01-09T00:00:00.00Z");

        WeekdaySchedule sched = new WeekdaySchedule.Builder().build();

        assertFalse(sched.shouldRun(now, lastRun, scheduledTime));
    }
}
