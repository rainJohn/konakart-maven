package is.tmsoftware.scheduler;

import is.tmsoftware.jobs.UpdateProductJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author bjarnig
 */
public class CronTrigger {

    public static void main(String[] args) throws SchedulerException {

        JobDetail job = JobBuilder.newJob(UpdateProductJob.class)
                .withIdentity("updateProduct", "updateProductsGroup").build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("updateProductTrigger", "updateProductsGroup111")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0/1 * * * * ?"))
                .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
