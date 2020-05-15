package mycompany;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class _0_UnscheduleAll {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            scheduler.clear();

            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
