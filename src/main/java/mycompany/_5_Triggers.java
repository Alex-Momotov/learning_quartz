package mycompany;

import org.quartz.Calendar;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.HolidayCalendar;

import java.util.Date;

//----------------------------------------------------------------------------------------------------------------------
//      Triggers
// We can add a trigger to shceduler without an associated job. The job to be triggered will be identified by the trigger's settings - e.g. job key.
// We can add a job to scheduler wihout an associated trigger. The job must be durable. The job will start firing once a trigger for it is added.

//      EndDate StartDate
// A trigger can have a particular start date and end date. This is a separate setting to the schedule.
// Start date determines when the trigger will start firing will come to the effect.
// End date determines the date after which the trigger will no longer fire.

//      Priority
// When multiple triggers are fired at exactly the same time and there are not enough Quearz worker threads to execute
// all their jobs, then the first N triggers with highest priority will be fired, where N is number of available threads.
// This only applies if those triggers fire at exactly the same time. A 10.59 trigger will always fire before 11.00 trigger.
// When a trigger’s job is detected to require recovery, its recovery is scheduled with the same priority as the original trigger

//      Misfire
// A misfire is when a trigger “misses” its firing time because of the scheduler being shutdown, or because there are no
// available threads in Quartz’s thread pool for executing the job. There are different misfire policies available.

//      Calendars
// Quartz calendar objects allow to exclude sections of time from being fired for a particular trigger.
// First you must register a calendar with the scheduler by doing scheduler.addCalendar()
// Then, when building a trigger you can specify that the trigger is modified by this calendar.
// There are multiple callendar implementations available in org.quartz.impl.calendar package.

//----------------------------------------------------------------------------------------------------------------------
public class _5_Triggers {
    final static String CALENDAR = "mycalendar";
    final static String JOB_NAME = "name1";
    final static String JOB_GROUP = "group1";

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.addCalendar(CALENDAR, simpleCalendar(), false, false);
            scheduler.start();

            action(scheduler);
            Thread.sleep(10_000);

            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // HolidayCalendar implementation of Quartz Calendar - it allows exclusion of specific days
    public static Calendar simpleCalendar() {
        HolidayCalendar cal = new HolidayCalendar();
        cal.addExcludedDate(new Date());
        return cal;
    }

    // Trigger that corresponds to a job with particular job key - name and group
    public static Trigger triggerForAParticularJob() {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .startNow()
                .forJob(JOB_NAME, JOB_GROUP)
                .build();
    }

    // Trigger that comes into/out of the effect on particular dates
    public static Trigger triggerWithStartEndDates() {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(1)
                    .repeatForever())
                .forJob(JOB_NAME, JOB_GROUP)
                .modifiedByCalendar(CALENDAR)
                .startAt(new Date(120, 1, 1))
                .endAt(new Date(120, 11, 1))
                .build();
    }

    // A trigger with priority
    public static Trigger triggerWithPriority() {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .forJob(JOB_NAME, JOB_GROUP)
                .withPriority(5)
                .build();
    }

    public static JobDetail simpleJob() {
        return JobBuilder.newJob(_2_HelloWorldJob.class)
                .withIdentity(JOB_NAME, JOB_GROUP)
                .storeDurably()
                .build();
    }

    public static void action(Scheduler scheduler) throws SchedulerException {
        scheduler.addJob(simpleJob(), true);
        scheduler.scheduleJob(triggerForAParticularJob());
    }

}
