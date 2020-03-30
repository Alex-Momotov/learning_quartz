package mycompany;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;



public class Example3Job implements Job {

    public Example3Job() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        List<Integer> list = (List<Integer>) jobDataMap.get("list");
        int num = jobDataMap.getInt("num");
        String string = jobDataMap.getString("string");
        long someLong = jobDataMap.getLong("direct");

        System.out.println("My list is " + list + " my num is " + num + " my string is " + string + " my long is " + someLong);
        System.out.println("My JobDetail key is: " + context.getJobDetail().getKey());
        System.out.println();
    }
}
