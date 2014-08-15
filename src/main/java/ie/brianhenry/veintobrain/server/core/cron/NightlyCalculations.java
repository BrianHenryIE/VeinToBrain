package ie.brianhenry.veintobrain.server.core.cron;

import ie.brianhenry.veintobrain.shared.representations.AnalyteResult;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;

import javax.inject.Inject;

import net.vz.mongodb.jackson.JacksonDBCollection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.DB;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.On;
 
/**
 * This is a scheduled task (cron) due to run at 4:30am every day. Its purpose is to calculate the statistics the client might need, i.e. yesterday's results
 * 
 * @author BrianHenry.ie
 * @see https://github.com/spinscale/dropwizard-jobs
 * @see http://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial-lesson-06
 */
@On("0 30 4 * * ?")
public class NightlyCalculations extends Job {

	JacksonDBCollection<AnalyteResult, String> analyteResults;
	JacksonDBCollection<AnalyteStat, String> analyteStats;

	@Inject
	public NightlyCalculations(DB db) {
		analyteResults = JacksonDBCollection.wrap(db.getCollection("analyteresult"), AnalyteResult.class, String.class);
		analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {
		// TODO Log the start
		
		// Process yesterday's results
		
		// If it's the end of the week, calculate the week's stats
		// If it's the start of a new month, calculate last month's stats
		
		

		
		

	}
}