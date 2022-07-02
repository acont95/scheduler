package scheduler;

import java.time.Clock;
import java.time.Instant;

public interface ScheduleDefine {
    Boolean isSynchronous();
    Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime);
    Boolean shouldDelete(Clock clock);
}

interface ScheduleBuilder {
    ScheduleDefine build();
}