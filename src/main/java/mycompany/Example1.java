package mycompany;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//----------------------------------------------------------------------------------------------------------------------
// Once you obtain a scheduler using StdSchedulerFactory.getDefaultScheduler(), your application will not terminate
// until you call scheduler.shutdown(), because there will be active threads.
public class Example1 {

    static final Logger logger = LoggerFactory.getLogger(Example1.class);

    public static void main(String[] args) {
        logger.info("test");
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
//
class Example2 {
    public static void main(String[] args) {
        System.out.println("Something");
    }
}
