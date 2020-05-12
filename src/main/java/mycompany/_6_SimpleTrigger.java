package mycompany;

import javafx.beans.binding.When;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.DateBuilder.evenHourDate;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class _6_SimpleTrigger {

    final static String JOB_NAME = "job1";
    final static String JOB_GROUP = "group1";

    final static String TRIGGER_NAME = "trigger1";
    final static String TRIGGER_GROUP = "group1";

//----------------------------------------------------------------------------------------------------------------------
//      Basics
// EndDate and StartDate work for both SimpleTrigger and CronTrigger.
// The properties of a SimpleTrigger include: a start-time, and end-time, a repeat count, and a repeat interval.
// Use SimpleTrigger when you want a job to execute at a point in time, then repeat a number of times with a given interval.

//      Misfire Instructions
// When building SimpleTriggers, you specify the misfire instruction as part of the simple schedule (via SimpleSchedulerBuilder).
// There are 6 misfire instruction policies available to SimpleScheduler, listed below as schedule builder methods
// .withMisfireHandlingInstructionFireNow()
// .withMisfireHandlingInstructionIgnoreMisfires()
// .withMisfireHandlingInstructionNextWithExistingCount()
// .withMisfireHandlingInstructionNextWithRemainingCount()
// .withMisfireHandlingInstructionNowWithExistingCount()
// .withMisfireHandlingInstructionNowWithRemainingCount()


    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            action(scheduler);
            Thread.sleep(10_000);

            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JobDetail simpleJob() {
        return JobBuilder.newJob(_2_HelloWorldJob.class)
                .withIdentity(JOB_NAME, JOB_GROUP)
                .storeDurably()
                .build();
    }

    // Trigger for a specific moment in time, with no repeats
    // As we can see, a trigger without a schedule fires only once upon it's startAt time
    public static SimpleTrigger simpleTrigger() {
        return (SimpleTrigger) newTrigger()
                .withIdentity(TRIGGER_NAME, TRIGGER_GROUP)
                .startAt(new Date())
                .build();
    }

    // Trigger for a specific moment in time, then repeating every ten seconds ten times
    // Note that 10 repeats will give a total of 11 firings
    public static SimpleTrigger simpleTrigger2() {
        return (SimpleTrigger) newTrigger()
                .withIdentity(TRIGGER_NAME, TRIGGER_GROUP)
                .startAt(new Date())
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(10))
                .build();
    }

    // Trigger that will fire once, five seconds in the future
    // Since there is no group in trigger's identity the group becomes the default group
    public static SimpleTrigger simpleTrigger3() {
        return (SimpleTrigger) newTrigger()
                .withIdentity(TRIGGER_NAME)
                .startAt(futureDate(5, DateBuilder.IntervalUnit.SECOND))
                .build();
    }

    // Trigger that will fire now, then repeat every five minutes, until the hour 22:00
    public static SimpleTrigger simpleTrigger4() {
        return newTrigger()
                .withIdentity(TRIGGER_NAME, TRIGGER_GROUP)
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(5)
                        .repeatForever())
                .endAt(dateOf(22, 0, 0))                            // today's date and 22:00 time
                .build();
    }

    // Trigger that will fire right now, then repeat every 2 hours, forever
    public static SimpleTrigger simpleTrigger5() {
        return newTrigger()
                .withIdentity(TRIGGER_NAME, TRIGGER_GROUP)
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(2)
                        .repeatForever())
                .build();
    }

    // Trigger with misfire instructions supplied via the schedule
    public static SimpleTrigger simpleTrigger6() {
        return (SimpleTrigger) newTrigger()
                .withIdentity(TRIGGER_NAME, TRIGGER_GROUP)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(1)
                        .withMisfireHandlingInstructionNowWithExistingCount()
                        .repeatForever())
                .build();
    }

    public static void action(Scheduler scheduler) throws SchedulerException {

        scheduler.scheduleJob(simpleJob(), simpleTrigger5());
    }

}
