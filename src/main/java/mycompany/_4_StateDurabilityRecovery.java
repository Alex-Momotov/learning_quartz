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

import static mycompany._4_Job.COUNTER;
import static mycompany._4_Job.NUM_EXCEPTIONS;

//----------------------------------------------------------------------------------------------------------------------
// Example of keeping state between executions of a particular JobDetail
// In this example we increment a counter each time our JobDetail executes

// Other properties of JobDetail
// Job durability   By default the JobDetail disappears when it is no longer assigned to a trigger. Setting durable to
//                  'true' like job.storeDurability() makes the JobDetail still live after all it's triggers disappear.
// Job recovery     Provides fail-over for JobDetail within a cluster. If while the job was executing, the machine
//                  running it crashed, the job will be re-executed by another node in Quartz cluster.

// If a job is being executed as recovery, the context.isRecovering() of JobExecutionContext will return true.
//----------------------------------------------------------------------------------------------------------------------

public class _4_StateDurabilityRecovery {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            action(scheduler);
        } catch (SchedulerException e) {
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
        jobMap.put(COUNTER, 1);
        jobMap.put(NUM_EXCEPTIONS, 3);

        JobDetail job = JobBuilder.newJob(_4_Job.class)
                .withIdentity("name2", "group1")
                .storeDurably()     // the job durability option
                .requestRecovery()  // the job recovery option - cluster fail-over
                .setJobData(jobMap)
                .build();

        scheduler.scheduleJob(job, simpleTrigger());
    }

}
