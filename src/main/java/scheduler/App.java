package scheduler;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class App 
{
    public static void main( String[] args )
    {
        SimulationConfig simulationConfig = new SimulationConfig(
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), 
            ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), 
            Duration.ofMinutes(1), 
            Duration.ZERO
        );
        SchedulerRuntime runtime = new SimulationRuntime(simulationConfig);
        Clock clock = runtime.getClock();
        Scheduler s = new Scheduler(2, clock);

        ScheduleDefine sched = PeriodSchedule.Builder.getInstance()
            .every(Period.ofDays(1))
            .build();

        class TestSchedulerCallable extends ScheduleCallable {

            public TestSchedulerCallable(Clock clock) {
                super(clock);
            }
            @Override
            public Void call() {
                // System.out.println(Instant.now(clock));
                System.out.println(LocalDate.ofInstant(Instant.now(clock), ZoneId.of("UTC")));
                return null;
            }
        }

        ScheduleDefine sched2 = PeriodSchedule.Builder.getInstance()
            .every(Period.ofWeeks(1))
            .build();

        class TestSchedulerCallable2 extends ScheduleCallable {

            public TestSchedulerCallable2(Clock clock) {
                super(clock);
            }
            @Override
            public Void call() {
                // System.out.println(Instant.now(clock));
                System.out.println("WEEK");
                return null;
            }
        }

        ScheduledTask task = new SingleScheduledTask("test", new TestSchedulerCallable(clock), sched, clock);
        ScheduledTask task2 = new SingleScheduledTask("test", new TestSchedulerCallable2(clock), sched2, clock);

        s.schedule(task);
        s.schedule(task2);
        runtime.start(s);
        s.shutDown();
    }
}
