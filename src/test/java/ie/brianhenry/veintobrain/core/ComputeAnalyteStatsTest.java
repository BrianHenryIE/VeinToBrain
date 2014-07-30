package ie.brianhenry.veintobrain.core;

import static org.junit.Assert.assertEquals;
import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComputeAnalyteStatsTest {

	@Test
	public void TestOne() {
		
		PSAdata pd = new PSAdata();
		

		List<AnalyteResult> analyteResultsList = pd.getResults();

		AnalyteStat overall = ComputeAnalyteStats.computeOverall(analyteResultsList, "psa");

		System.out.println("overall.getMean() (arithmentic mean) : " + overall.getMean());

		List<AnalyteStat> allDailyAnalyteStats = new ArrayList<AnalyteStat>();

		HashMap<LocalDate, List<String>> hm = new HashMap<LocalDate, List<String>>();

		for (AnalyteResult r : analyteResultsList) {
			if (hm.get(r.getDate()) == null)
				hm.put(r.getDate(), new ArrayList<String>());
			hm.get(r.getDate()).add(r.getResult());
		}

		List<AnalyteDate> allVaildAnalyteDates = new ArrayList<AnalyteDate>();
		
		System.out.println("hm.size() : " + hm.size());
		for (LocalDate day : hm.keySet()) {
			// System.out.println(day.toString() + " : " + hm.get(day));
			AnalyteDate d = new AnalyteDate("psa", day.toDate(), hm.get(day).toArray(new String[hm.get(day).size()]));
			AnalyteStat s = ComputeAnalyteStats.computeDay(d, "psa");
			if (s != null) {
				// System.out.println("adding to all daily");
				allDailyAnalyteStats.add(s);
				allVaildAnalyteDates.add(d);
			} else {
				System.out.println("invalid day");
				System.out.println(day.toString() + " : " + hm.get(day));
			}

		}
		
//		List<String> goodYear = new ArrayList<String>();
//		for(AnalyteDate gd : allVaildAnalyteDates){
//			goodYear.addAll(Arrays.asList(gd.getResults()));
//		}
//		AnalyteDate year = new AnalyteDate("psa", new Date(), goodYear.toArray(new String[goodYear.size()]));
//		
//		AnayteStat year = new AnayteStat
		

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

		
		// Get an addressable list of the daily stats so we can ask for the previous days' stats
		// for the moving mean
		HashMap<LocalDate, AnalyteStat> days = new HashMap<LocalDate, AnalyteStat>();

		for (int i = 0; i < allDailyAnalyteStats.size(); i++) {
			days.put(new LocalDate(allDailyAnalyteStats.get(i).getIncludedDates().get(0)), allDailyAnalyteStats.get(i));
		}

		LocalDate firstDay = new LocalDate(2013, 7, 1);
		LocalDate nextDate = firstDay.plusDays(1);
		LocalDate lastDate = new LocalDate(2014, 7, 1);

		final int MOVINGMEANDAYS = 20;
		
		
		while (nextDate.isBefore(lastDate)) {

			if (days.get(nextDate) != null) {
				double movingSum = 0;
				int movingSumCount = 0;

				for (int i = 1; movingSum < MOVINGMEANDAYS && nextDate.minusDays(i).isAfter(firstDay); i++) {
					if (days.get(nextDate.minusDays(i)) != null) {
						movingSum += days.get(nextDate.minusDays(i)).getPercentile(0.5);
						movingSumCount++;
					}
				}

				double movingMean = movingSum / movingSumCount;

				if (Double.isNaN(movingMean)) {
					System.out.println("NaN : sum:" + movingSum + ", count:" + movingSumCount);
				}

				days.get(nextDate).setMovingMean(Integer.toString(MOVINGMEANDAYS), movingMean);

				System.out.println(nextDate.toString() + ", " + movingMean);
			}
			nextDate = nextDate.plusDays(1);
		}

		
		
		
		
		ObjectMapper mapper = new ObjectMapper();
	    try {
			System.out.println(mapper.writeValueAsString(overall));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

	
	
	@Test
	public void standardDeviationTest() {

		String[] readingsS = { "11.1", "14.5", "17.9", "8.7", "15.7", "12.3", "2.8", "14.9", "4.5", "7.8", "6.6",
				"10.6", "23.0", "19.0", "4.7", "9.5", "7.1", "13.5", "9.5", "3.5", "16.1", "21.7", "11.2", "9.8",
				"13.8", "20.7", "6.6", "4.2", "6.2", "19.9", "15.9", "21.8", "9.0" };

		List<Double> readings = new ArrayList<Double>();

		for (String s : readingsS)
			readings.add(Double.parseDouble(s));

		// =STDEV.P(B13:B45)
		// 5.774553914
		double excel = 5.774553914;

		double sd = ComputeAnalyteStats.standardDeviation(readings);
		assertEquals(excel, sd, 0.001);

	}
}
