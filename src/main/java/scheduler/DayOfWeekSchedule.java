package scheduler;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
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

    interface Every {
        Builder every(DayOfWeek dayOfWeek);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private DayOfWeek dayOfWeek;
        private ZoneId timeZone = Clock.systemUTC().getZone();


        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
    
        public Builder every(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        @Override
        public DayOfWeekSchedule build() {
            return new DayOfWeekSchedule(dayOfWeek, timeZone);
        }
    } 
}
