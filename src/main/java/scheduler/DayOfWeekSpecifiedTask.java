package scheduler;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZonedDateTime;

public final class DayOfWeekSpecifiedTask implements ScheduleDefine{
    private final DayOfWeek dayOfWeek;
    private final Boolean synchronous;

    private DayOfWeekSpecifiedTask(DayOfWeek dayOfWeek, Boolean synchronous) {
        this.dayOfWeek = dayOfWeek;
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
        return now.getDayOfWeek() == dayOfWeek;
    }

    private Boolean shouldRunNormal(ZonedDateTime now, Clock clock, Instant lastRun) {
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, clock.getZone()).toLocalDate())) && (now.getDayOfWeek() == dayOfWeek);
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    interface Every {
        Builder every(DayOfWeek dayOfWeek);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private DayOfWeek dayOfWeek;
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
    
        public Builder every(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        @Override
        public DayOfWeekSpecifiedTask build() {
            return new DayOfWeekSpecifiedTask(dayOfWeek, synchronous);
        }
    } 
}
