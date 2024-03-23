package scheduler;

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
    public Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);

        return (
            !dateTime.toLocalDate().isEqual(ZonedDateTime.ofInstant(lastRun, timeZone).toLocalDate()) 
            && (dateTime.toLocalTime().isAfter(localTime) || dateTime.toLocalTime().equals(localTime))
        );
    }

    @Override
    public Boolean shouldRunInit(Instant now, Instant scheduledTime) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now, timeZone);
        return dateTime.toLocalTime().isAfter(localTime) || dateTime.toLocalTime().equals(localTime);
    }

    @Override
    public Boolean shouldDelete(Instant now) {
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
