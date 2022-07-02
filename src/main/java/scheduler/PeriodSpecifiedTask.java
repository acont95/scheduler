package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.Period;
import java.time.ZonedDateTime;

public final class PeriodSpecifiedTask implements ScheduleDefine{
    private final Period intervalPeriod;
    private final Boolean synchronous;
    private final Period initialDelay;
    

    private PeriodSpecifiedTask(Period intervalPeriod, Boolean synchronous, Period initialDelay, long durationMilli) {
        this.intervalPeriod = intervalPeriod;
        this.synchronous = synchronous;
        this.initialDelay = initialDelay;
    }

    @Override
    public Boolean isSynchronous() {
        return synchronous;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dt = ZonedDateTime.now(clock);
        ZonedDateTime lastRunZoned = ZonedDateTime.ofInstant(lastRun, clock.getZone());
        ZonedDateTime scheduledTimeZoned = ZonedDateTime.ofInstant(scheduledTime, clock.getZone());
        if (lastRun != null) {
            return shouldRunNormal(dt, lastRunZoned);
        } else {
            return shouldRunInitial(dt, scheduledTimeZoned);
        }
    }

    private Boolean shouldRunInitial(ZonedDateTime now, ZonedDateTime scheduledTimeZoned) {
        return now.isAfter(scheduledTimeZoned.plus(initialDelay)) || now.equals(scheduledTimeZoned.plus(initialDelay));
    }

    private Boolean shouldRunNormal(ZonedDateTime now, ZonedDateTime lastRunZoned) {
        return now.isAfter(lastRunZoned.plus(intervalPeriod)) || now.equals(lastRunZoned.plus(intervalPeriod));
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public interface Every {
        Builder every(Period intervaPeriod);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private Period intervalPeriod;
        private Boolean synchronous = false;
        private Period initialDelay = null;
        private long durationMilli;

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
    
        public Builder synchronous() {
            synchronous = true;
            return this;
        }
    
        public Builder every(Period intervalPeriod) {
            this.intervalPeriod = intervalPeriod;
            this.initialDelay = intervalPeriod;
            return this;
        }

        public Builder initialDelay(Period initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        @Override
        public PeriodSpecifiedTask build() {
            return new PeriodSpecifiedTask(intervalPeriod, synchronous, initialDelay, durationMilli);
        }
    } 
}
