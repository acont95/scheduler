package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZonedDateTime;

public final class MonthSpecifiedTask implements ScheduleDefine{
    private final Month month;
    private final Boolean synchronous;
    // private Instant lastRun = null;    

    private MonthSpecifiedTask(Month month, Boolean synchronous) {
        this.month = month;
        this.synchronous = synchronous;
    }

    @Override
    public Boolean isSynchronous() {
        return synchronous;
    }

    // @Override
    // public void setLastRun(Instant lastRun) {
    //     this.lastRun = lastRun;
    // }

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
        return now.getMonth() == month;
    }

    private Boolean shouldRunNormal(ZonedDateTime now, Clock clock, Instant lastRun) {
        return (! YearMonth.from(now).equals(YearMonth.from(ZonedDateTime.ofInstant(lastRun, clock.getZone())))) && (now.getMonth() == month);
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
    
        public Builder every(Month month) {
            this.month = month;
            return this;
        }

        @Override
        public MonthSpecifiedTask build() {
            return new MonthSpecifiedTask(month, synchronous);
        }
    } 
}
