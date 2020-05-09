package mycompany;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//----------------------------------------------------------------------------------------------------------------------
//      Instantiating Scheduler
// Once you obtain a scheduler using StdSchedulerFactory.getDefaultScheduler(), your application will not terminate
// until you call scheduler.shutdown(), because there will be active threads.
// Once a scheduler is shutdown, it cannot be restarted without being re-instantiated.
public class _1_Scheduler {

    static final Logger logger = LoggerFactory.getLogger(_1_Scheduler.class);

    public static void main(String[] args) {
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            // shut it down - until we do this the application won't quit
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

}

//----------------------------------------------------------------------------------------------------------------------

