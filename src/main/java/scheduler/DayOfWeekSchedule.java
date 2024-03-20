package scheduler;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class DayOfWeekSchedule implements ScheduleDefine{
    private final DayOfWeek dayOfWeek;
    private final ZoneId timeZone;

    private DayOfWeekSchedule(DayOfWeek dayOfWeek, ZoneId timeZone) {
        this.dayOfWeek = dayOfWeek;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);

        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, now.getZone()).toLocalDate())) && (DayOfWeek.from(now) == dayOfWeek);
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);

        return DayOfWeek.from(now) == dayOfWeek;
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder {
        private DayOfWeek dayOfWeek;
        private ZoneId timeZone = ZoneOffset.UTC;

        public Builder(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public DayOfWeekSchedule build() {
            return new DayOfWeekSchedule(dayOfWeek, timeZone);
        }
    } 
}
