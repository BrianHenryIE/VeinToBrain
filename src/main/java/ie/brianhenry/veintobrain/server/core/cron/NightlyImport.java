package ie.brianhenry.veintobrain.server.core.cron;

import ie.brianhenry.veintobrain.shared.representations.AnalyteResult;

import javax.inject.Inject;

import net.vz.mongodb.jackson.JacksonDBCollection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.DB;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.On;
 
/**
 * This is a scheduled task (cron) due to run at 2:30am every day. Its purpose is to pull raw data from the 
 * hospital information system.
 * 
 * @author BrianHenry.ie
 * @see https://github.com/spinscale/dropwizard-jobs
 * @see http://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial-lesson-06
 */
@On("0 30 2 * * ?")
public class NightlyImport extends Job {

	JacksonDBCollection<AnalyteResult, String> analyteResults;

	@Inject
	public NightlyImport(DB db) {
		analyteResults = JacksonDBCollection.wrap(db.getCollection("analyteresult"), AnalyteResult.class, String.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {
		// logic run every time and time again
		// TODO use actual logger
		System.out.println("2:30am run");

		//
		// PSAdata pd = new PSAdata();
		// for (int i = 1; i < 13 && i!=5 ; i++) {
		// AnalyteStat as = ComputeAnalyteStats.computeMonth(pd.getMonth(i),
		// "psa", i);
		// WriteResult<AnalyteStat, String> result = analyteStats.insert(as);
		// }
		//
		//
		// for(AnalyteResult r : pd.getResults()){
		// WriteResult<AnalyteResult, String> result = analyteResults.insert(r);
		// }
		//
		// System.out.println("db populated");

	}
}