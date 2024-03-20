package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class MonthDaySchedule implements ScheduleDefine{
    private final MonthDay monthDay;
    private final ZoneId timeZone;

    private MonthDaySchedule(MonthDay monthDay, ZoneId timeZone) {
        this.monthDay = monthDay;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, timeZone).toLocalDate())) && 
            (MonthDay.from(now).equals(monthDay));
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return MonthDay.from(now).equals(monthDay);
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder{
        private MonthDay monthDay;
        private ZoneId timeZone = Clock.systemUTC().getZone();

        public Builder(MonthDay monthDay) {
            this.monthDay = monthDay;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public MonthDaySchedule build() {
            return new MonthDaySchedule(monthDay, timeZone);
        }
    } 
}
