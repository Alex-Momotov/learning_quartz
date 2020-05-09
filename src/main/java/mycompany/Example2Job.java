package mycompany;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

//----------------------------------------------------------------------------------------------------------------------
// By implementing Job interface we are able to create executable jobs as JobDetail
// JobExecutionContext has refenrences to Scheduler that executed it, Trigger that triggered it, JobDetail, and others.

// The only exception that you're allowed to throw inside execute() method is JobExecutionException.
// Therefore you should wrap the contents of execute() method within try-catch and throw JobExecutionException.

// JobExecutionExceptions tells Quartz how to handle the failed job. For example, we can set .setRefireImmediately(true)
// on the exception instance if we want the job to be instantly re-executed.

//----------------------------------------------------------------------------------------------------------------------


public class Example2Job implements Job {

    // Classes implementing Job must have no-arguments constructor
    public Example2Job() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.out.println("Hello world ");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }


    }
}