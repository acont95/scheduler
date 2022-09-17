package scheduler;

import java.time.Duration;
import java.time.ZonedDateTime;

public record SimulationConfig (
    ZonedDateTime start,
    ZonedDateTime end,
    Duration step,
    Duration stepPause
) {}


