package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class LocalTimeSpecifiedTask implements ScheduleDefine{
    private final LocalTime localTime;
    private final Boolean synchronous;
    private final ZoneId timeZone;

    private LocalTimeSpecifiedTask(LocalTime localTime, Boolean synchronous, ZoneId timeZone) {
        this.localTime = localTime;
        this.synchronous = synchronous;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean isSynchronous() {
        return synchronous;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dt = ZonedDateTime.now(clock);
        if (timeZone != null) {
            dt = dt.withZoneSameInstant(timeZone);
        }
        if (lastRun != null) {
            return shouldRunNormal(dt, clock, lastRun);
        } else {
            return shouldRunInitial(dt);
        }
    }

    private Boolean shouldRunInitial(ZonedDateTime now) {
        return now.toLocalTime().isAfter(localTime);
    }

    private Boolean shouldRunNormal(ZonedDateTime now, Clock clock, Instant lastRun) {
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, clock.getZone()).toLocalDate())) && (now.toLocalTime().isAfter(localTime));
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public interface Every {
        Builder every(LocalTime localTime);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private LocalTime localTime;
        private Boolean synchronous = false;
        private ZoneId timeZone = null;

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
        
    
        public Builder synchronous() {
            synchronous = true;
            return this;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }
    
        public Builder every(LocalTime localTime) {
            this.localTime = localTime;
            return this;
        }

        @Override
        public LocalTimeSpecifiedTask build() {
            return new LocalTimeSpecifiedTask(localTime, synchronous, timeZone);
        }
    } 
}
