package ie.brianhenry.veintobrain.core.cron;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.On;

// 2:30 am
// http://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial-lesson-06
@On("0 30 2 * * ?")
public class EveryTestJob extends Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {
		// logic run every time and time again
		System.out.println("2:30am run");

	}
}