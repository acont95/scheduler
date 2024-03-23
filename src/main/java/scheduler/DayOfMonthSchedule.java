package scheduler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.threeten.extra.DayOfMonth;

public final class DayOfMonthSchedule implements ScheduleDefine{
    private final DayOfMonth dayOfMonth;
    private final ZoneId timeZone;

    private DayOfMonthSchedule(DayOfMonth dayOfMonth, ZoneId timeZone) {
        this.dayOfMonth = dayOfMonth;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);

        return (
            !dateTime.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, dateTime.getZone()).toLocalDate())) 
            && (DayOfMonth.from(dateTime).equals(dayOfMonth)
        );
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);

        return DayOfMonth.from(dateTime).equals(dayOfMonth);
    }

    @Override
    public Boolean shouldDelete(Instant now) {
        return false;
    }

    public static class Builder {
        private DayOfMonth dayOfMonth;
        private ZoneId timeZone = ZoneOffset.UTC;

        public Builder(DayOfMonth dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public DayOfMonthSchedule build() {
            return new DayOfMonthSchedule(dayOfMonth, timeZone);
        }
    } 
}
