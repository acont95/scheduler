package scheduler;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZonedDateTime;

public final class WeekdaySpecifiedTask implements ScheduleDefine{
    private final Boolean synchronous;

    private WeekdaySpecifiedTask(Boolean synchronous) {
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
        return (now.getDayOfWeek() != DayOfWeek.SATURDAY) && (now.getDayOfWeek() != DayOfWeek.SUNDAY);
    }

    private Boolean shouldRunNormal(ZonedDateTime now, Clock clock, Instant lastRun) {
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, clock.getZone()).toLocalDate())) && ((now.getDayOfWeek() != DayOfWeek.SATURDAY) && (now.getDayOfWeek() != DayOfWeek.SUNDAY));
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder implements ScheduleBuilder{
        private Boolean synchronous = false;

        private Builder() {
        }

        public static Builder getInstance() {
            return new Builder();
        }
        
    
        public Builder synchronous() {
            synchronous = true;
            return this;
        }

        @Override
        public WeekdaySpecifiedTask build() {
            return new WeekdaySpecifiedTask(synchronous);
        }
    } 
}
