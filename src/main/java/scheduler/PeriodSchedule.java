package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;

public final class PeriodSchedule implements ScheduleDefine{
    private final Period intervalPeriod;
    private final Period initialDelay;
    private final ZoneId timeZone;
    

    private PeriodSchedule(Period intervalPeriod, Period initialDelay, ZoneId timeZone) {
        this.intervalPeriod = intervalPeriod;
        this.initialDelay = initialDelay;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        Instant now = Instant.now(clock);
        return now.isAfter(lastRun.plus(intervalPeriod)) || now.equals(lastRun.plus(intervalPeriod));
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        Instant now = Instant.now(clock);
        return now.isAfter(scheduledTime.plus(initialDelay)) || now.equals(scheduledTime.plus(initialDelay));
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public interface Every {
        Builder every(Period intervalPeriod);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private Period intervalPeriod;
        private Period initialDelay = Period.ZERO;
        private ZoneId timeZone = Clock.systemUTC().getZone();

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }
    
        public Builder every(Period intervalPeriod) {
            this.intervalPeriod = intervalPeriod;
            return this;
        }

        public Builder initialDelay(Period initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        @Override
        public PeriodSchedule build() {
            return new PeriodSchedule(intervalPeriod, initialDelay, timeZone);
        }
    } 
}
