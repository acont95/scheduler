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
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, now.getZone()).toLocalDate())) && 
            (MonthDay.from(now) == monthDay);
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return MonthDay.from(now) == monthDay;
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    interface Every {
        Builder every(MonthDay monthDay);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private MonthDay monthDay;
        private ZoneId timeZone = Clock.systemUTC().getZone();

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
    
        public Builder every(MonthDay monthDay) {
            this.monthDay = monthDay;
            return this;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        @Override
        public MonthDaySchedule build() {
            return new MonthDaySchedule(monthDay, timeZone);
        }
    } 
}
