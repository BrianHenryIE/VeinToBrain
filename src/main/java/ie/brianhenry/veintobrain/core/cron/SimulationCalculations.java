package ie.brianhenry.veintobrain.core.cron;

import ie.brianhenry.veintobrain.core.ComputeAnalyteStats;
import ie.brianhenry.veintobrain.representations.AnalyteConfig;
import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;

import org.joda.time.LocalDate;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.OnApplicationStart;

/**
 * This is a practice class for calculating with our test data. Should be in a
 * test package, then.
 * 
 * @author BrianHenry.ie
 * @see https://github.com/spinscale/dropwizard-jobs
 * @see http
 *      ://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial
 *      -lesson-06
 */
@OnApplicationStart
public class SimulationCalculations extends Job {

	JacksonDBCollection<AnalyteConfig, String> analyteConfigs;
	JacksonDBCollection<AnalyteResult, String> analyteResults;
	JacksonDBCollection<AnalyteStat, String> analyteStats;

	@Inject
	public SimulationCalculations(DB db) {
//		analyteConfigs = JacksonDBCollection.wrap(db.getCollection("analyteconfig"), AnalyteConfig.class, String.class);
//		analyteResults = JacksonDBCollection.wrap(db.getCollection("analyteresult"), AnalyteResult.class, String.class);
//		analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {

		// http://mongojack.org/tutorial.html
/*		
		analyteStats.drop();
		BasicDBObject indexObj = new BasicDBObject();
		indexObj.put("analyteType", 1);
		indexObj.put("analytePeriod", 1);
		analyteStats.createIndex(indexObj);
		
		
		List<AnalyteResult> analyteResultsList = new ArrayList<AnalyteResult>();

		DBCursor<AnalyteResult> cursor = analyteResults.find().is("analyteType", "psa");
		while (cursor.hasNext()) {
			AnalyteResult nextObject = cursor.next();
			analyteResultsList.add(nextObject);
		}

		AnalyteStat overall = ComputeAnalyteStats.computeOverall(analyteResultsList, "psa");

		System.out.println("overall.getMean() (arithmentic mean) : " + overall.getMean());

		List<AnalyteStat> allDailyAnalyteStats = new ArrayList<AnalyteStat>();

		HashMap<LocalDate, List<String>> hm = new HashMap<LocalDate, List<String>>();

		for (AnalyteResult r : analyteResultsList) {
			if (hm.get(r.getDate()) == null)
				hm.put(r.getDate(), new ArrayList<String>());
			hm.get(r.getDate()).add(r.getResult());
		}

		System.out.println("hm.size() : " + hm.size());
		for (LocalDate day : hm.keySet()) {
			// System.out.println(day.toString() + " : " + hm.get(day));
			AnalyteDate d = new AnalyteDate("psa", day.toDate(), hm.get(day).toArray(new String[hm.get(day).size()]));
			AnalyteStat s = ComputeAnalyteStats.computeDay(d, "psa");
			if (s != null) {
				// System.out.println("adding to all daily");
				allDailyAnalyteStats.add(s);
				WriteResult<AnalyteStat, String> result = analyteStats.insert(s);
			} else {
				System.out.println("invalid day");
				System.out.println(day.toString() + " : " + hm.get(day));
			}

		}

		System.out.println("allDailyAnalyteStats.size() : " + allDailyAnalyteStats.size());

		List<Double> allMedians = new ArrayList<Double>();

		double sumOfMedian = 0.0;
		for (AnalyteStat stat : allDailyAnalyteStats) {
			sumOfMedian += stat.getPercentile(0.5);
			allMedians.add(stat.getPercentile(0.5));
		}

		double meanOfMedians = sumOfMedian / allDailyAnalyteStats.size();

		System.out.println("meanOfMedians : " + meanOfMedians);

		double sdMedians = ComputeAnalyteStats.standardDeviation(allMedians);

		System.out.println("sd of the medians : " + sdMedians);

		double medianCV = sdMedians / meanOfMedians;

		System.out.println("medianCV : " + medianCV);

		//
		// for(AnalyteStat stat : allDailyAnalyteStats){
		// // compare this to the following two months
		// for(AnalyteStat compareStat : allDailyAnalyteStats){
		//
		//
		// }
		//
		// }
		//
		//

		HashMap<LocalDate, AnalyteStat> days = new HashMap<LocalDate, AnalyteStat>();

		for (int i = 0; i < allDailyAnalyteStats.size(); i++) {
			days.put(new LocalDate(allDailyAnalyteStats.get(i).getIncludedDates().get(0)), allDailyAnalyteStats.get(i));
		}

		LocalDate firstDay = new LocalDate(2013, 7, 1);
		LocalDate nextDate = firstDay.plusDays(1);
		LocalDate lastDate = new LocalDate(2014, 7, 1);

		while (nextDate.isBefore(lastDate)) {

			if (days.get(nextDate) != null) {
				double movingSum = 0;
				int movingSumCount = 0;

				for (int i = 1; movingSum < 7 && nextDate.minusDays(i).isAfter(firstDay); i++) {
					if (days.get(nextDate.minusDays(i)) != null) {
						movingSum += days.get(nextDate.minusDays(i)).getPercentile(0.5);
						movingSumCount++;
					}
				}

				double movingMean = movingSum / movingSumCount;

				if (Double.isNaN(movingMean)) {
					System.out.println("NaN : sum:" + movingSum + ", count:" + movingSumCount);
				}

				days.get(nextDate).setMovingMean("7", movingMean);

				System.out.println(nextDate.toString() + ", " + movingMean);
			}
			nextDate = nextDate.plusDays(1);
		}

		
		
		
		
		ObjectMapper mapper = new ObjectMapper();
	    try {
			System.out.println(mapper.writeValueAsString(days.get(nextDate.minusDays(12))));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  */
	}
}