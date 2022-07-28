package scheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
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
        ZonedDateTime now = ZonedDateTime.now(clock);
        return (! now.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, now.getZone()).toLocalDate())) &&
            ((now.toLocalTime().isAfter(localTime)) || (now.toLocalTime().equals(localTime)));
    }

    @Override
    public Boolean shouldRunInit(Clock clock, Instant scheduledTime) {
        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(clock), timeZone);
        return now.toLocalTime().isAfter(localTime);
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
        private ZoneId timeZone = Clock.systemUTC().getZone();

        private Builder() {
        }

        public static Every getInstance() {
            return new Builder();
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
        public LocalTimeSchedule build() {
            return new LocalTimeSchedule(localTime, timeZone);
        }
    } 
}
