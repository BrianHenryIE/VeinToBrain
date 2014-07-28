package ie.brianhenry.veintobrain.core.cron;

import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.On;

// 2:30 am
// http://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial-lesson-06
@On("0 30 2 * * ?")
public class NightlyJob extends Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {
		// logic run every time and time again
		System.out.println("2:30am run");
		
		
		// We need to get a refernce to our db here
		
//	     JacksonDBCollection<AnalyteResult, String> analyteResults = JacksonDBCollection.wrap(db.getCollection("analyteresult"), AnalyteResult.class, String.class);
//	        
//	        JacksonDBCollection<AnalyteStat, String> analyteStat = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);

//		
//		JacksonDBCollection<AnalyteResult, String> analyteResults;
//		JacksonDBCollection<AnalyteStat, String> analyteStats;
//
//		public AnalyteResource(JacksonDBCollection<AnalyteResult, String> analyteResults,
//				JacksonDBCollection<AnalyteStat, String> analyteStat) {
//
//			this.analyteResults = analyteResults;
//			this.analyteStats = analyteStat;

//		PSAdata pd = new PSAdata();
//		for (int i = 1; i < 13 && i!=5 ; i++) {
//			AnalyteStat as = ComputeAnalyteStats.computeMonth(pd.getMonth(i), "psa", i);
//			WriteResult<AnalyteStat, String> result = analyteStats.insert(as);
//		}
//		
//		
//		for(AnalyteResult r : pd.getResults()){
//			WriteResult<AnalyteResult, String> result = analyteResults.insert(r);
//		}


	}
}