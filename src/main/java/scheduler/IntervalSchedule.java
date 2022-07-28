package scheduler;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public final class IntervalSchedule implements ScheduleDefine{
    private final Boolean alignedToEpoch;
    private final Duration intervalDuration;
    private final Duration initialDelay;
    private final long durationMilli;

    private IntervalSchedule(Boolean alignedToEpoch, Duration intervalDuration, Duration initialDelay, long durationMilli) {
        this.alignedToEpoch = alignedToEpoch;
        this.intervalDuration = intervalDuration;
        this.initialDelay = initialDelay;
        this.durationMilli = durationMilli;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        Instant now = Instant.now(clock);
        if (alignedToEpoch) {
            return shouldRunAlgined(now, lastRun);
        }
        else {
            return shouldRunNormal(now, lastRun);
        }
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        Instant now = Instant.now(clock);
        if (!alignedToEpoch) {
            return now.isAfter(scheduledTime.plus(initialDelay)) || now.equals(scheduledTime.plus(initialDelay));
        } else {
            long milli = now.toEpochMilli();
            long scheduledTimeMilli = scheduledTime.toEpochMilli(); //time task was scheduled

            long nextMin = scheduledTimeMilli + initialDelay.toMillis(); 
            long rem = nextMin % durationMilli;
            if (rem != 0) {
                return milli >= nextMin - rem + durationMilli;
            }
            else {
                return milli >= nextMin;
            }
        }
    }

    private Boolean shouldRunNormal(Instant now, Instant lastRun) {
        return now.isAfter(lastRun.plus(intervalDuration)) || now.equals(lastRun.plus(intervalDuration));
    }

    private Boolean shouldRunAlgined(Instant now, Instant lastRun) {
        long milli = now.toEpochMilli();
        long durationMilli = intervalDuration.toMillis();
        long lastRunMilli = lastRun.toEpochMilli();
        return (milli >= (lastRunMilli - (lastRunMilli % durationMilli) + durationMilli));
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public interface Every {
        Builder every(Duration intervalDuration);
    }

    public static class Builder implements ScheduleBuilder, Every{
        private Boolean alignedToEpoch = false;
        private Duration intervalDuration;
        private Duration initialDelay = null;
        private long durationMilli;

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
        }
        
        public Builder alignedToEpoch() {
            alignedToEpoch = true;
            durationMilli = intervalDuration.toMillis();
            return this;
        }
    
        public Builder every(Duration intervalDuration) {
            this.intervalDuration = intervalDuration;
            this.initialDelay = intervalDuration;
            return this;
        }

        public Builder initialDelay(Duration initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        @Override
        public IntervalSchedule build() {
            return new IntervalSchedule(alignedToEpoch, intervalDuration, initialDelay, durationMilli);
        }
    } 
}
