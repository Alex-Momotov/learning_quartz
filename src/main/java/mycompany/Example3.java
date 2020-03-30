package mycompany;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Arrays;

//----------------------------------------------------------------------------------------------------------------------
//      Jobs
// Remember, when creating JobDetail we're not giving it an instance of Job, but merely giving it the class (.class).
// Each and every time scheduler executes that JobDetail, it creates a new instance of the Job class before calling its execute(..) method.
// When the execution is finished, refenrences to the Job instance are dropped and instance is garbage collected.
// Ramification 1 - Jobs must have a no-argument constructor.
// Ramification 2 - Jobs must be stateless, they are garbage collected between executions anyways.

// In order to provide resources to your job, and keep track of state between executions we must use JobDataMap.

//----------------------------------------------------------------------------------------------------------------------
//      JobDataMap
// JobDataMap can hold any amount of objects that you wish to make available to your job, but they must be serialisable.
// JobDataMap implements java's Map interface.

public class Example3 {

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

    public static Trigger simpleTrigger() {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .startNow()
                .build();
    }

    public static void action(Scheduler scheduler) throws SchedulerException {
        // Creating JobDataMap in order to pass resources to our job
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("list", Arrays.asList(1, 2, 3));
        jobDataMap.put("num", 10);
        jobDataMap.put("string", "some string");

        JobDetail job = JobBuilder.newJob(Example3Job.class)
                .withIdentity("name1", "group1")    // This will become JobDetail key retriveable via context.getJobDetail().getKey() in the job
                .usingJobData(jobDataMap)           // We can provide instance of JobDataMap
                .usingJobData("direct", 123L)       // Or we add values to JobDataMap within JobDetail directly
                .build();

        scheduler.scheduleJob(job, simpleTrigger());
    }

}
