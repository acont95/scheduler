package scheduler;

import java.time.Instant;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class MonthSchedule implements ScheduleDefine{
    private final Month month;
    private final ZoneId timeZone;

    private MonthSchedule(Month month, ZoneId timeZone) {
        this.month = month;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return (
            !YearMonth.from(dateTime).equals(YearMonth.from(ZonedDateTime.ofInstant(lastRun, timeZone)))
            && dateTime.getMonth() == month
        );
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return dateTime.getMonth() == month;
    }

    @Override
    public Boolean shouldDelete(Instant now) {
        return false;
    }

    public static class Builder{
        private Month month;
        private ZoneId timeZone = ZoneOffset.UTC;

        public Builder(Month month) {
            this.month = month;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public MonthSchedule build() {
            return new MonthSchedule(month, timeZone);
        }
    } 
}
