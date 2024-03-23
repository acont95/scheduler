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
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return (
            !dateTime.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, dateTime.getZone()).toLocalDate())
            && dateTime.getDayOfWeek() != DayOfWeek.SATURDAY
            && dateTime.getDayOfWeek() != DayOfWeek.SUNDAY
        );
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return dateTime.getDayOfWeek() != DayOfWeek.SATURDAY && dateTime.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    @Override
    public Boolean shouldDelete(Instant now) {
        return false;
    }

    public static class Builder {
        private ZoneId timeZone = Clock.systemUTC().getZone();

        public Builder() {}

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public WeekdaySchedule build() {
            return new WeekdaySchedule(timeZone);
        }
    } 
}
