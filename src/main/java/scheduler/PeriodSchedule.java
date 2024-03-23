package scheduler;

import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;

public final class PeriodSchedule implements ScheduleDefine{
    private final Period intervalPeriod;
    private final Period initialDelay;
    private final ZoneId timeZone;
    private final boolean truncate;
    private final TemporalUnit temporalUnit;
    

    private PeriodSchedule(
        Period intervalPeriod, 
        Period initialDelay, 
        ZoneId timeZone, 
        boolean truncate, 
        TemporalUnit temporalUnit
    ) {
        this.intervalPeriod = intervalPeriod;
        this.initialDelay = initialDelay;
        this.timeZone = timeZone;
        this.truncate = truncate;
        this.temporalUnit = temporalUnit;
    }

    @Override
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        if (!truncate) {
            return shouldRunNormal(now, lastRun);
        } else {
            return shouldRunTruncated(now, lastRun);
        }
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        if (!truncate) {
            return now.isAfter(scheduledTime.plus(initialDelay)) || now.equals(scheduledTime.plus(initialDelay));
        } else {
            return shouldRunTruncatedInit(now, scheduledTime);
        }
    }

    private Boolean shouldRunNormal(Instant now, Instant lastRun) {
        return now.isAfter(lastRun.plus(intervalPeriod)) || now.equals(lastRun.plus(intervalPeriod));
    }

    private boolean shouldRunTruncated(Instant now, Instant lastRun) {
        ZonedDateTime zonedNow = ZonedDateTime.ofInstant(now, timeZone);
        return (
            zonedNow.isAfter(ZonedDateTime.ofInstant(lastRun, timeZone).truncatedTo(temporalUnit).plus(intervalPeriod))
            || zonedNow.equals(ZonedDateTime.ofInstant(lastRun, timeZone).truncatedTo(temporalUnit).plus(intervalPeriod))
        );
    }

    private boolean shouldRunTruncatedInit(Instant now, Instant scheduledTime) {
        ZonedDateTime zonedNow = ZonedDateTime.ofInstant(now, timeZone);
        return (
            shouldRunTruncated(now, scheduledTime.plus(initialDelay))
            || zonedNow.equals(ZonedDateTime.ofInstant(scheduledTime, timeZone).truncatedTo(temporalUnit).plus(initialDelay))
        );
    }

    @Override
    public Boolean shouldDelete(Instant now) {
        return false;
    }

    public static class Builder{
        private Period intervalPeriod;
        private Period initialDelay;
        private ZoneId timeZone = ZoneOffset.UTC;
        private boolean truncate = false;
        private TemporalUnit temporalUnit;

        public Builder(Period intervalPeriod) {
            this.intervalPeriod = intervalPeriod;
            this.initialDelay = intervalPeriod;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public Builder initialDelay(Period initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public Builder truncateTo(TemporalUnit unit) {
            truncate = true;
            temporalUnit = unit;
            return this;
        }

        public PeriodSchedule build() {
            return new PeriodSchedule(intervalPeriod, initialDelay, timeZone, truncate, temporalUnit);
        }
    } 
}
