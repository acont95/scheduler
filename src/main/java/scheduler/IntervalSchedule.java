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
        } else {
            return shouldRunNormal(now, lastRun);
        }
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        Instant now = Instant.now(clock);
        if (!alignedToEpoch) {
            return now.isAfter(scheduledTime.plus(initialDelay)) || now.equals(scheduledTime.plus(initialDelay));
        } else {
            // Test with exact
            // return shouldRun(clock, scheduledTime.plus(initialDelay), scheduledTime);
            return shouldRunAlginedInit(now, scheduledTime);
        }
    }

    private Boolean shouldRunNormal(Instant now, Instant lastRun) {
        return now.isAfter(lastRun.plus(intervalDuration)) || now.equals(lastRun.plus(intervalDuration));
    }

    private Boolean shouldRunAlgined(Instant now, Instant lastRun) {
        long milli = now.toEpochMilli();
        long lastRunMilli = lastRun.toEpochMilli();
        return (milli >= (lastRunMilli - (lastRunMilli % durationMilli) + durationMilli));
    }

    private Boolean shouldRunAlginedInit(Instant now, Instant scheduledTime) {
        long milli = now.toEpochMilli();
        long scheduledTimeMilli = scheduledTime.plus(initialDelay).toEpochMilli();
        long alignedTime = scheduledTimeMilli - (scheduledTimeMilli % durationMilli);

        if (alignedTime >= scheduledTimeMilli) {
            return milli >= alignedTime;
        } else {
            return milli >= alignedTime + durationMilli;
        }
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder {
        private Boolean alignedToEpoch = false;
        private Duration intervalDuration;
        private Duration initialDelay;
        private long durationMilli;

        public Builder(Duration intervalDuration) {
            this.intervalDuration = intervalDuration;
            durationMilli = intervalDuration.toMillis();
            this.initialDelay = intervalDuration;
        }
        
        public Builder alignedToEpoch() {
            alignedToEpoch = true;
            return this;
        }

        public Builder initialDelay(Duration initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public IntervalSchedule build() {
            return new IntervalSchedule(alignedToEpoch, intervalDuration, initialDelay, durationMilli);
        }
    } 
}
