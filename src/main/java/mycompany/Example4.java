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

//----------------------------------------------------------------------------------------------------------------------
// Example of keeping state between executions of a particular JobDetail
// In this example we increment a counter each time our JobDetail executes

//----------------------------------------------------------------------------------------------------------------------

public class Example4 {

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

    public static Trigger simpleTrigger() {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .startNow()
                .build();
    }

    public static void action(Scheduler scheduler) throws SchedulerException {
        JobDataMap jobMap = new JobDataMap();
        jobMap.put("counter", 1);

        JobDetail job = JobBuilder.newJob(Example4Job.class)
                .withIdentity("name1", "group1")
                .setJobData(jobMap)
                .build();

        scheduler.scheduleJob(job, simpleTrigger());
    }

}
