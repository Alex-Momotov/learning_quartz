package mycompany;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

//----------------------------------------------------------------------------------------------------------------------
//      Hello World Job
// 'Hello world' job, triggered now, then every 2 sec, repeats forever. HelloJob is defined in separate class.
public class _2_HelloWorld {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            action(scheduler);
            Thread.sleep(15_000);

            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void action(Scheduler scheduler) throws SchedulerException {
        // define the job and tie it to our HelloJob class
        JobDetail helloJob = JobBuilder.newJob(_2_HelloWorldJob.class)
                .withIdentity("job1", "group1")
                .build();

        // Schedule that fires every 2 seconds and repeats forever
        ScheduleBuilder<SimpleTrigger> scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2)
                .repeatForever();

        // Trigger with provided schedule that starts now
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        scheduler.scheduleJob(helloJob, trigger);
    }
}

//----------------------------------------------------------------------------------------------------------------------
//      Key classes
// Scheduler        Main API for interacting with scheduler. Used to add / remove / list jobs and triggers.

// Job              Interface. Class that implements Job defines what work will actually be executed.
// JobBuilder       Uses a Job to instantiate JobDetail.
// JobDetail        The instantiated Job.

// TriggerBuilder   Builds and instantiates Trigger.
// Trigger          Defines schedule upon which a given Job will be executed.

//----------------------------------------------------------------------------------------------------------------------
//      Trigger Types
// SimpleTrigger    Fires at a given time and repeats N times.
// CronTrigger      Fires on calendar-like schedules - “every Friday, at noon” or “at 10:15 on the 10th day of every month.”

//----------------------------------------------------------------------------------------------------------------------
//      Identity - Naming Trigger or Job
// A trigger or a job name consists of a 'name' and 'group'.
// A job/trigger name must be unique within the group.

// Group is used to organise jobs/triggers together, e.g. 'reporting jobs' or 'maintanance jobs'.  While 'name'
// identifies each specific job/trigger.

//----------------------------------------------------------------------------------------------------------------------
