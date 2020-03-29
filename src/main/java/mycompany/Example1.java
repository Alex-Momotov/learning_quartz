package mycompany;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

//----------------------------------------------------------------------------------------------------------------------
// Once you obtain a scheduler using StdSchedulerFactory.getDefaultScheduler(), your application will not terminate
// until you call scheduler.shutdown(), because there will be active threads.
public class Example1 {

    static final Logger logger = LoggerFactory.getLogger(Example1.class);

    public static void main(String[] args) {
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            // shut it down - until we do this the application won't quit
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

}

//----------------------------------------------------------------------------------------------------------------------
// 'Hello world' job, triggered now, then every 2 sec, repeats forever. HelloJob is defined in separate class.
class Example2 {

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
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();

        // Schedule that fires every 2 seconds and repeats forever
        ScheduleBuilder<SimpleTrigger> scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().
                withIntervalInSeconds(2)
                .repeatForever();

        // Trigger with provided schedule that starts now
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}

//----------------------------------------------------------------------------------------------------------------------


