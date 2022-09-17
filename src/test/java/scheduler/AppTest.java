// package scheduler;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;

// import java.time.Clock;
// import java.time.DayOfWeek;
// import java.time.Duration;
// import java.time.Instant;
// import java.time.LocalDateTime;
// import java.time.Month;
// import java.time.ZoneId;
// import java.time.ZoneOffset;
// import java.util.ArrayList;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;

// import org.junit.Test;

// public class AppTest 
// {

//     @Test
//     public void shouldOnlyExecuteInNextInterval()
//     {
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Duration.ofSeconds(1))
//             .schedule(new TestCallable());
        
//         // clock = Clock.offset(clock, Duration.ofSeconds(2));
//         ArrayList<Future<?>> results = s.runPendingCollect();
//         try {
//             s.executorService.awaitTermination(1, TimeUnit.SECONDS);
//         } catch (InterruptedException e){

//         }
//         assertTrue(results.isEmpty());
//     }

//     @Test
//     public void clockIncrementLessThanIntervalDoesNothing()
//     {
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Duration.ofSeconds(1))
//             .schedule(new TestCallable());
        
//         s.updateClock(Clock.offset(clock, Duration.ofMillis(500)));
//         ArrayList<Future<?>> results = s.runPendingCollect();

//         try {
//             s.executorService.awaitTermination(1, TimeUnit.SECONDS);
//         } catch (InterruptedException e){

//         }
//         assertTrue(results.isEmpty());
//     }

//     @Test
//     public void clockIncrementExactlyIntervalRunsJob()
//     {
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Duration.ofSeconds(1))
//             .schedule(new TestCallable());
        
//         s.updateClock(Clock.offset(clock, Duration.ofSeconds(1)));
//         ArrayList<Future<?>> results = s.runPendingCollect();

//         try {
//             s.executorService.awaitTermination(1, TimeUnit.SECONDS);
//         } catch (InterruptedException e){

//         }
//         assertFalse(results.isEmpty());
//     }

//     @Test
//     public void clockIncrementGreaterIntervalRunsJob()
//     {
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Duration.ofSeconds(1))
//             .schedule(new TestCallable());
        
//         s.updateClock(Clock.offset(clock, Duration.ofMillis(1500)));
//         ArrayList<Future<?>> results = s.runPendingCollect();

//         try {
//             s.executorService.awaitTermination(1, TimeUnit.SECONDS);
//         } catch (InterruptedException e){

//         }
//         assertFalse(results.isEmpty());
//     }

//     @Test
//     public void alignedIntervalSetsNextTimeCorrectly()
//     {
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.50Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Duration.ofSeconds(1))
//             .alignToEpoch()
//             .schedule(new TestCallable());
        
//         assertEquals(Instant.parse("2000-01-01T00:00:01.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void dayOfWeekNextTimeSetCorrectly()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:01:01.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(DayOfWeek.WEDNESDAY)
//             .schedule(new TestCallable());
        
//         assertEquals(Instant.parse("2000-01-05T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void dayOfWeekSameAsScheduledDaySchedulesImmediatley()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-05T12:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(DayOfWeek.WEDNESDAY)
//             .schedule(new TestCallable());
        
//         assertEquals(Instant.parse("2000-01-05T12:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void lastRunSameAsScheduledDaySameAsCurrentDaySchedulesNext()
//     {
//         // Wednesday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-05T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(DayOfWeek.WEDNESDAY)
//             .schedule(new TestCallable());
        
//         s.runPending();
        
//         assertEquals(Instant.parse("2000-01-12T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void monthOfYearNextTimeSetCorrectly()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.FEBRUARY)
//             .schedule(new TestCallable());
        
//         assertEquals(Instant.parse("2000-02-01T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void monthOfYearNextTimeSetCorrectlyWhenMonthIsNextYear()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-02-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.JANUARY)
//             .schedule(new TestCallable());
        
//         assertEquals(Instant.parse("2001-01-01T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void sameMonthAsCurrentSchedulesImmediatley()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-12T12:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.JANUARY)
//             .schedule(new TestCallable());
        
//         assertEquals(Instant.parse("2000-01-12T12:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void lastRunSameAsScheduledMonthSameAsCurrentMonthSchedulesNext()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.JANUARY)
//             .schedule(new TestCallable());
        
//         s.runPending();
        
//         assertEquals(Instant.parse("2001-01-01T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void monthAndDayNextTimeSetCorrectly()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.FEBRUARY)
//             .every(DayOfWeek.MONDAY)
//             .schedule(new TestCallable());
                
//         assertEquals(Instant.parse("2000-02-07T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void monthAndDayNextTimeSetCorrectlyNewYear()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-12-31T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.DECEMBER)
//             .every(DayOfWeek.MONDAY)
//             .schedule(new TestCallable());
                
//         assertEquals(Instant.parse("2001-12-03T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void sameDayAndMonthAsCurrentSchedulesImmediatley()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-02-07T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.FEBRUARY)
//             .every(DayOfWeek.MONDAY)
//             .schedule(new TestCallable());
                
//         assertEquals(Instant.parse("2000-02-07T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void lastRunSameAsScheduledDayAndMonthSameAsCurrentDayAndMonthSchedulesNext()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-03T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .every(Month.JANUARY)
//             .every(DayOfWeek.MONDAY)
//             .schedule(new TestCallable());
        
//         s.runPending();
        
//         assertEquals(Instant.parse("2001-01-01T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void scheduleAtSpecificDateTime()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .at(LocalDateTime.of(2000, 1, 2, 0, 0, 0))
//             .schedule(new TestCallable());
                
//         assertEquals(Instant.parse("2000-01-02T00:00:00.00Z"), s.getJobs().get(0).nextRun.toInstant(ZoneOffset.UTC));
//     }

//     @Test
//     public void scheduleAtSpecificDateTimeShouldDeleteAfterRun()
//     {
//         // Saturday
//         Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00.00Z"), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
//         Scheduler s = new Scheduler(clock, 1);
//         s.createJob("test")
//             .at(LocalDateTime.of(2000, 1, 2, 0, 0, 0))
//             .schedule(new TestCallable());

//         s.runPending();
//         assertEquals(0, s.getJobs().size());
//     }
// }
