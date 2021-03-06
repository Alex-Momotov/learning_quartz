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

import java.io.Serializable;
import java.util.Arrays;

import static mycompany._3_Job.COMPLETION_MESSAGE_PARAM;
import static mycompany._3_Job.DB_CONNECTION_PARAM;
import static mycompany._3_Job.INTEGER_LIST_PARAM;
import static mycompany._3_Job.KAFKA_PRODUCER_PARAM;
import static mycompany._3_Job.REPETITION_NUM_PARAM;

//----------------------------------------------------------------------------------------------------------------------
//      Jobs
// Remember, when creating JobDetail we're not giving it an instance of Job, but merely giving it the class (.class).
// Each and every time scheduler executes that JobDetail, it creates a new instance of the Job implementation class before calling its execute(..) method.
// When the execution is finished, refenrences to the Job implementation instance are dropped and instance is garbage collected.
// Ramification 1 - Jobs must have a no-argument constructor.
// Ramification 2 - Jobs must be stateless, they are garbage collected between executions anyways.

// In order to provide resources to your job, and keep track of state between executions we must use JobDataMap.
// For saving state between jobs use @DisallowConcurrentExecution and @PersistJobDataAfterExecution annotations on Job class.

//----------------------------------------------------------------------------------------------------------------------
//      JobDataMap
// JobDataMap can hold any amount of objects that you wish to make available to your job, but they must be serialisable.
// JobDataMap implements java's Map interface.

public class _3_JobDataMap {

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
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .startNow()
                .build();
    }

    public static void action(Scheduler scheduler) throws SchedulerException {
        // Pre-existing resources setup at application startup
        DbConnection dbConnection = new DbConnection();
        KafkaProducer kafkaProducer = new KafkaProducer();

        // Creating JobDataMap in order to pass resources to our job
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(INTEGER_LIST_PARAM, Arrays.asList(1, 2, 3));
        jobDataMap.put(REPETITION_NUM_PARAM, 10);
        jobDataMap.put(COMPLETION_MESSAGE_PARAM, "Work is complete.");
        jobDataMap.put(DB_CONNECTION_PARAM, dbConnection);
        jobDataMap.put(KAFKA_PRODUCER_PARAM, kafkaProducer);

        JobDetail job = JobBuilder.newJob(_3_Job.class)
                .withIdentity("name1", "group1")    // This will become JobDetail key retriveable via context.getJobDetail().getKey() in the Job class
                .usingJobData(jobDataMap)           // We can provide instance of JobDataMap
                .usingJobData("direct", 123L)       // Or we add values to JobDataMap within JobDetail directly
                .build();

        scheduler.scheduleJob(job, simpleTrigger());
    }

    static class DbConnection implements Serializable {/*I represent an open database connection*/}
    static class KafkaProducer implements Serializable {/*I represent a running Kafka producer*/}

}

//----------------------------------------------------------------------------------------------------------------------
//      JobDataMap per trigger
// JobDataMap can be associated with triggers too. Then the trigger will pass the map to the job. This is useful if you
// want the job to be executed differently depending on what triggered it.

// The JobDataMap found in JobExecutionContext is a joined map between JobDataMap found in JobDetail and JobTrigger.
// Values in the trigger map override values in JobDetail map when their keys are the same.

//----------------------------------------------------------------------------------------------------------------------
//      Job instances
// You can create a single job class (impl Job interface) then store multiple instances of it within the scheduler by
// creating multiple instances of JobDetails - each with it's own set of properties and JobDataMap and adding them all
// to the scheduler.
//----------------------------------------------------------------------------------------------------------------------
//      Definitions
// 'Job class'                                          Class implementing the Job interface
// 'job definition' or 'JobDetail instance'             Instance of JobDetail
// 'job instance' or 'instance of a job definition'     Execution of JobDetail in a particular moment in time

//----------------------------------------------------------------------------------------------------------------------

