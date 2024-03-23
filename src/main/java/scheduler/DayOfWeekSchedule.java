package scheduler;

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
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);

        return (
            !dateTime.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, dateTime.getZone()).toLocalDate())) 
            && (DayOfWeek.from(dateTime) == dayOfWeek
        );
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);

        return DayOfWeek.from(dateTime) == dayOfWeek;
    }

    @Override
    public Boolean shouldDelete(Instant now) {
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
