package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class MonthSchedule implements ScheduleDefine{
    private final Month month;
    private final ZoneId timeZone;

    private MonthSchedule(Month month, ZoneId timeZone) {
        this.month = month;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return (!YearMonth.from(now).equals(YearMonth.from(ZonedDateTime.ofInstant(lastRun, timeZone)))) && 
            (now.getMonth() == month);
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return now.getMonth() == month;
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder{
        private Month month;
        private ZoneId timeZone = Clock.systemUTC().getZone();

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
