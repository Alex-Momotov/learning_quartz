package mycompany;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import java.util.List;


public class Example3Job implements Job {

    static final String INTEGER_LIST_PARAM          = "integer_list";
    static final String REPETITION_NUM_PARAM        = "repetition_number";
    static final String COMPLETION_MESSAGE_PARAM    = "completion_message";
    static final String DB_CONNECTION_PARAM         = "db_connection";
    static final String KAFKA_PRODUCER_PARAM        = "kafka_producer";

    private List<Integer> integerList;
    private int repetitionNum;
    private String completionMessage;
    private Example3.DbConnection dbConnection;
    private Example3.KafkaProducer kafkaProducer;

    public Example3Job() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            System.out.println("Starting the job: " + context.getJobDetail().getKey());     // name and group of JobDetail

            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();     // JobDataMap coming from JobDetail. Typically we want to use this one.
            JobDataMap mergedJobDataMap = context.getMergedJobDataMap();        // JobDataMap merged from JobDetail and Trigger

            setUpJobResources(jobDataMap);
            performJob();
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }


    public void setUpJobResources(JobDataMap jobDataMap) {
        this.integerList = (List<Integer>) jobDataMap.get(INTEGER_LIST_PARAM);
        this.repetitionNum = jobDataMap.getInt(REPETITION_NUM_PARAM);
        this.completionMessage = jobDataMap.getString(COMPLETION_MESSAGE_PARAM);
        this.dbConnection = (Example3.DbConnection) jobDataMap.get(DB_CONNECTION_PARAM);
        this.kafkaProducer = (Example3.KafkaProducer) jobDataMap.get(KAFKA_PRODUCER_PARAM);
    }

    private void performJob() {
        System.out.println("getting data from the database");
        for (int i = 0; i < repetitionNum; i++) {
            System.out.println(integerList);
        }
        System.out.println("produced data to Kafka");
        System.out.println(completionMessage);

    }

}
