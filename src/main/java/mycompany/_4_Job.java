package mycompany;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

//----------------------------------------------------------------------------------------------------------------------
// When using @PersistJobDataAfterExecution it is usually a good idea to also use @DisallowConcurrentExecution to avoid
// race conditions if two the JobDetail is executed concurrently

// Here we illustrate how .setRefireImmediately(true) on the instance of JobExecutionExeption works. Try to set it to
// true / false and see the difference.

//----------------------------------------------------------------------------------------------------------------------
@DisallowConcurrentExecution        // Means two jobs of the same JobDetail - or job key - cannot be executed concurrently
@PersistJobDataAfterExecution       // Means changes to JobDataMap will be persisted after this JobDetail finishes executing
public class _4_Job implements Job {

    static final String COUNTER = "counter";
    static final String NUM_EXCEPTIONS = "retries";

    public _4_Job() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            int counter = jobMap.getIntValue(COUNTER);
            int numExceptions = jobMap.getIntValue(NUM_EXCEPTIONS);

            System.out.println(counter);

            if (counter == 3 && numExceptions > 0) {
                jobMap.put(NUM_EXCEPTIONS, --numExceptions);
                throw new RuntimeException();
            }

            jobMap.put(COUNTER, ++counter);
        } catch (Exception e) {
            JobExecutionException jobEx = new JobExecutionException();
            jobEx.setRefireImmediately(true);
            throw jobEx;
        }
    }
}
