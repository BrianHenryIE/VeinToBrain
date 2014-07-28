package ie.brianhenry.veintobrain.core.cron;

import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import javax.inject.Inject;

import net.vz.mongodb.jackson.JacksonDBCollection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.DB;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.On;

// 2:30 am
// https://github.com/spinscale/dropwizard-jobs
// http://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial-lesson-06
@On("0 40 18 * * ?")
public class NightlyJob extends Job {

	JacksonDBCollection<AnalyteResult, String> analyteResults;
	JacksonDBCollection<AnalyteStat, String> analyteStats;

	@Inject
	public NightlyJob(DB db) {
		analyteResults = JacksonDBCollection.wrap(db.getCollection("analyteresult"), AnalyteResult.class, String.class);
		analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {
		// logic run every time and time again
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