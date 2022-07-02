package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.MonthDay;
import java.time.ZonedDateTime;

public final class MonthDaySpecifiedTask implements ScheduleDefine{
    private final MonthDay monthDay;
    private final Boolean synchronous;

    private MonthDaySpecifiedTask(MonthDay monthDay, Boolean synchronous) {
        this.monthDay = monthDay;
        this.synchronous = synchronous;
    }

    @Override
    public Boolean isSynchronous() {
        return synchronous;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dt = ZonedDateTime.now(clock);
        if (lastRun != null) {
            return shouldRunNormal(dt, clock, lastRun);
        } else {
            return shouldRunInitial(dt);
        }
    }

    private Boolean shouldRunInitial(ZonedDateTime now) {
        return MonthDay.from(now) == monthDay;
    }

    private Boolean shouldRunNormal(ZonedDateTime now, Clock clock, Instant lastRun){
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, clock.getZone()).toLocalDate())) && (MonthDay.from(now) == monthDay);
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    interface Every {
        Builder every(MonthDay monthDay);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private MonthDay monthDay;
        private Boolean synchronous = false;

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
        
    
        public Builder synchronous() {
            synchronous = true;
            return this;
        }
    
        public Builder every(MonthDay monthDay) {
            this.monthDay = monthDay;
            return this;
        }

        @Override
        public MonthDaySpecifiedTask build() {
            return new MonthDaySpecifiedTask(monthDay, synchronous);
        }
    } 
}
