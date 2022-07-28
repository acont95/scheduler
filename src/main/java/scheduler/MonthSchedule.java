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
        return (! YearMonth.from(now).equals(YearMonth.from(ZonedDateTime.ofInstant(lastRun, now.getZone())))) && 
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

    interface Every {
        Builder every(Month month);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private Month month;
        private ZoneId timeZone = Clock.systemUTC().getZone();

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
    
        public Builder every(Month month) {
            this.month = month;
            return this;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        @Override
        public MonthSchedule build() {
            return new MonthSchedule(month, timeZone);
        }
    } 
}
