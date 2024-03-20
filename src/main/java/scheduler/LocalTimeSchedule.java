package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class LocalTimeSchedule implements ScheduleDefine{
    private final LocalTime localTime;
    private final ZoneId timeZone;

    private LocalTimeSchedule(LocalTime localTime, ZoneId timeZone) {
        this.localTime = localTime;
        this.timeZone = timeZone;
    }

    @Override
    public Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);

        return (
            !now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, timeZone).toLocalDate()) 
            && (now.toLocalTime().isAfter(localTime) || now.toLocalTime().equals(localTime))
        );
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return now.toLocalTime().isAfter(localTime) || now.toLocalTime().equals(localTime);
    }

    @Override
    public Boolean shouldDelete(Clock clock) {
        return false;
    }

    public static class Builder {
        private LocalTime localTime;
        private ZoneId timeZone = ZoneOffset.UTC;

        public Builder(LocalTime localTime) {
            this.localTime = localTime;
        }

        public Builder withZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public LocalTimeSchedule build() {
            return new LocalTimeSchedule(localTime, timeZone);
        }
    } 
}
