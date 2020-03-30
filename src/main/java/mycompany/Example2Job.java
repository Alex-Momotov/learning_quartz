package mycompany;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

// By implementing Job interface we are able to create executable jobs as JobDetail
// JobExecutionContext has refenrences to Scheduler that executed it, Trigger that triggered it, JobDetail, and others.
public class Example2Job implements Job {

    // Classes implementing Job must have no-arguments constructor
    public Example2Job() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello world ");
    }
}