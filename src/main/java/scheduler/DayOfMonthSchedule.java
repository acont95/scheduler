package scheduler;

import java.time.Clock;
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
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);

        return (
            !now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, now.getZone()).toLocalDate())) 
            && (DayOfMonth.from(now).equals(dayOfMonth)
        );
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);

        return DayOfMonth.from(now).equals(dayOfMonth);
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
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
