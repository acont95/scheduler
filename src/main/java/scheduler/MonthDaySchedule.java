package scheduler;

import java.time.Instant;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class MonthDaySchedule implements ScheduleDefine{
    private final MonthDay monthDay;
    private final ZoneId timeZone;

    private MonthDaySchedule(MonthDay monthDay, ZoneId timeZone) {
        this.monthDay = monthDay;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return (
            !dateTime.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, timeZone).toLocalDate())
            && MonthDay.from(dateTime).equals(monthDay)
        );
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return MonthDay.from(dateTime).equals(monthDay);
    }

    @Override
    public Boolean shouldDelete(Instant now) {
        return false;
    }

    public static class Builder{
        private MonthDay monthDay;
        private ZoneId timeZone = ZoneOffset.UTC;

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
