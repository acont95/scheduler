package scheduler;

import java.time.Instant;

public interface ScheduleDefine {
    Boolean shouldRun(Instant now, Instant lastRun, Instant scheduledTime);
    Boolean shouldRunInit(Instant now, Instant scheduledTime);
    Boolean shouldDelete(Instant now);
}
