package scheduler;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class WeekdaySchedule implements ScheduleDefine{
    private final ZoneId timeZone;

    private WeekdaySchedule(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, now.getZone()).toLocalDate())) && 
            ((now.getDayOfWeek() != DayOfWeek.SATURDAY) && (now.getDayOfWeek() != DayOfWeek.SUNDAY));

    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return (now.getDayOfWeek() != DayOfWeek.SATURDAY) && (now.getDayOfWeek() != DayOfWeek.SUNDAY);
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder implements ScheduleBuilder{
        private ZoneId timeZone = Clock.systemUTC().getZone();

        private Builder() {
        }

        public static Builder getInstance() {
            return new Builder();
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        @Override
        public WeekdaySchedule build() {
            return new WeekdaySchedule(timeZone);
        }
    } 
}
