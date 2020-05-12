package mycompany;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.time.Month;

public class _7_CronTrigger {

    final static String JOB_NAME = "job1";
    final static String JOB_GROUP = "group1";

    final static String TRIGGER_NAME = "trigger1";
    final static String TRIGGER_GROUP = "group1";

//----------------------------------------------------------------------------------------------------------------------
//      Basics
// Just like with SimpleTrigger we can specify startAt and endAt to control when the schedule comes in force for CronTrigger.

//----------------------------------------------------------------------------------------------------------------------
//      Cron Expressions
// Cron expressions are needed to configure CronTrigger. The expressions are strings made up of seven sub expressions.

// Format and legal values:
// 1. Seconds           0-59
// 2. Minutes           0-59
// 3. Hours             0-23
// 4. Day-of-Month      1-31 (but need to be careful how many days there are in a given month)
// 5. Month             0-11 or JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
// 6. Day-of-Week       1-7 (1 is Sunday) or SUN, MON, TUE, WED, THU, FRI and SAT
// 7. Year (optional)

// Format: 1. Seconds 2. Minutes 3. Hours 4. Day-of-Month 5. Month 6. Day-of-Week 7. Year (optional)
// “0 0 12 ? * WED”             means every Wednesday at 12:00:00 pm

// Lists and ranges such as: “MON-FRI”, “MON,WED,FRI”, “MON-WED,SAT”
// "0 0 12 ? * MON-WED,SAT"     means every Mon, Tue, Wed, and Sat at 12:00:00 pm
// A list for minutes could be 3,23,43 meaning at 3 min, 23 min and 43 min of every hour

// The 'offset/interval' expression is used to specify increments of values.
// E.g. '0/15' in the minutes field means 0 min, 15 min, 30 min, 45 min of every hour (offset 0 min and interval 15 min)
// E.g. '3/20' in the minutes field means 3 min, 23 min, 43 min of every hour (offset 3 min and interval 20 min)
// '/35' means every 35th minute of each hour, starting at minute zero, so at 0 min and 35 min

// Character '*' means every value of a field, e.g. every month if it's in the month field or every day of the week if it's in Day-of-week field
// "0 0 12 ? * MON"             means every month on Mon at 12:00:00 pm

// Character '?' is allowed for day-of-month and day-of-week fields and means "no specific value".
// This is used to specify something in one of the two fields, but not the other.

// Character ‘L’ is allowed for the day-of-month and day-of-week fields.
// Value “L” in the day-of-month field means “the last day of the month” - day 31 for January, day 28 for February on non-leap years.
// If used in the day-of-week field by itself, it simply means “7” or “SAT”

// The ‘#’ is used to specify the Nth weekday of the month. E.g. the value of “6#3” or “FRI#3” in the day-of-week field means “the third Friday of the month”.

//----------------------------------------------------------------------------------------------------------------------
//      Example Cron Expressions
// 0 0/5 * * * ?                Fires every 5 minutes
// 10 0/5 * * * ?               Fires every 5 minutes at 10 seconds after the minute (i.e. 10:00:10 am, 10:05:10 am, etc.).
// 0 30 10-13 ? * WED,FRI       Fires at 10:30, 11:30, 12:30, and 13:30, on every Wednesday and Friday
// 0 0/30 8-9 5,20 * ?         Fires every half hour between the hours of 8 am and 10 am on the 5th and 20th of every month. Note that the trigger will NOT fire at 10:00 am, just at 8:00, 8:30, 9:00 and 9:30

//----------------------------------------------------------------------------------------------------------------------
//      Multiple Triggers For Same Job
// Some scheduling requirements are too complicated to express with a single trigger - such as “every 5 minutes between
// 9:00 am and 10:00 am, and every 20 minutes between 1:00 pm and 10:00 pm”. The solution in this scenario is to simply
// create two triggers, and register both of them to run the same job.

//----------------------------------------------------------------------------------------------------------------------


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

    public static void action(Scheduler scheduler) throws SchedulerException {

//        scheduler.scheduleJob(simpleJob(), );
    }

}
