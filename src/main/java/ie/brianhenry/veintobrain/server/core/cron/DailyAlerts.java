package ie.brianhenry.veintobrain.server.core.cron;

import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;

import javax.inject.Inject;

import net.vz.mongodb.jackson.JacksonDBCollection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.DB;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.On;

/**
 * This is a scheduled task (cron) due to run at 8am every day. Its purpose is
 * to alert the doctor if the recently calculated results have changed
 * significantly
 * 
 * @author BrianHenry.ie
 * @see https://github.com/spinscale/dropwizard-jobs
 * @see http
 *      ://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial
 *      -lesson-06
 */
@On("0 00 8 * * ?")
public class DailyAlerts extends Job {

	JacksonDBCollection<AnalyteStat, String> analyteStats;

	@Inject
	public DailyAlerts(DB db) {
		analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {
		// TODO: log
		
		// Compare the recent stats

	}
}