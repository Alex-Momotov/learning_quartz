package mycompany;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;


// When using @PersistJobDataAfterExecution it is usually a good idea to also use @DisallowConcurrentExecution to avoid
// race conditions if two the JobDetail is executed concurrently
@DisallowConcurrentExecution        // Means two jobs of the same JobDetail - or job key - cannot be executed concurrently
@PersistJobDataAfterExecution       // Means changes to JobDataMap will be persisted after this JobDetail finishes executing
public class Example4Job implements Job {

    public Example4Job() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();

        int currVal = jobMap.getIntValue("counter");
        System.out.println(currVal);

        jobMap.put("counter", ++currVal);
    }
}
