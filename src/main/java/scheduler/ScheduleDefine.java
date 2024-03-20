package scheduler;

import java.time.Clock;
import java.time.Instant;

public interface ScheduleDefine {
    Boolean shouldRun(Clock clock, Instant lastRun, Instant scheduledTime);
    Boolean shouldRunInit(Clock clock, Instant scheduledTime);
    Boolean shouldDelete(Clock clock);
}
