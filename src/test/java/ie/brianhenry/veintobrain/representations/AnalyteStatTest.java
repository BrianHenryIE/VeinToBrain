package ie.brianhenry.veintobrain.representations;

import static org.junit.Assert.assertEquals;
import ie.brianhenry.veintobrain.core.ComputeAnalyteStats;
import ie.brianhenry.veintobrain.resources.PSAdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

public class AnalyteStatTest {

	@Test
	public void sorting() {
		PSAdata pd = new PSAdata();
		HashMap<LocalDate, AnalyteStat> allDailyAnalyteStats = new HashMap<LocalDate, AnalyteStat>();

		List<AnalyteResult> analyteResultsList;
		List<AnalyteDate> allVaildAnalyteDates = new ArrayList<AnalyteDate>();
		List<AnalyteDate> allInVaildAnalyteDates = new ArrayList<AnalyteDate>();

		//the RAW data are here
		analyteResultsList = pd.getResults();

		//hm contains the results divided by day.
		//The analytes are still mixed up though.
		HashMap<LocalDate, List<String>> hm = new HashMap<LocalDate, List<String>>();

		for (AnalyteResult r : analyteResultsList) {
			if (hm.get(r.getDate()) == null)
				hm.put(r.getDate(), new ArrayList<String>());
			hm.get(r.getDate()).add(r.getResult());
		}

		for (LocalDate day : hm.keySet()) {
			AnalyteDate d = new AnalyteDate("psa", day.toDate(), hm.get(day));
			AnalyteStat s = ComputeAnalyteStats.computeDay(d, "psa");
			if (s.getIsValid()) {
				allDailyAnalyteStats.put(day, s);
				allVaildAnalyteDates.add(d);
			} else
				allInVaildAnalyteDates.add(d);
		}

		ComputeAnalyteStats.getMovingMeanOfMedian(allDailyAnalyteStats, 50);
		ComputeAnalyteStats.getMovingMeanOfMedian(allDailyAnalyteStats, 7);
		ComputeAnalyteStats.getMovingMeanOfMedian(allDailyAnalyteStats, 20);

		ComputeAnalyteStats.getMovingMean(allDailyAnalyteStats, 50);
		ComputeAnalyteStats.getMovingMean(allDailyAnalyteStats, 7);
		ComputeAnalyteStats.getMovingMean(allDailyAnalyteStats, 20);

		List<AnalyteStat> stats = new ArrayList<AnalyteStat>();

		for(AnalyteStat as : allDailyAnalyteStats.values())
			stats.add(as);

		
		Collections.sort(stats);
		
		for(AnalyteStat a : stats)
			System.out.println(a.getIncludedDates().get(0).toString());
		
	}
	
	
	@SuppressWarnings("deprecation")
	public void constructor() {

		Date statDate = new Date();
		statDate.setYear(2009);
		statDate.setMonth(0);
		statDate.setDate(2);

		String statType = "Folate";

		String[] readings = { "11.1", "14.5", "17.9", "8.7", "15.7", "12.3", "2.8", "14.9", "4.5", "7.8", "6.6",
				"10.6", "23.0", "19.0", "4.7", "9.5", "7.1", "13.5", "9.5", "3.5", "16.1", "21.7", "11.2", "9.8",
				"13.8", "20.7", "6.6", "4.2", "6.2", "19.9", "15.9", "21.8", "9.0" };

		AnalyteDate day = new AnalyteDate(statType, statDate, Arrays.asList(readings));

		// Maybe this should take in an array of number and just have getters
		AnalyteStat folate02Jan2009 = ComputeAnalyteStats.computeDay(day, statType);

		double min = 2.8;
		double i2p5th = 3.36;
		double i25th = 7.1;
		double median = 11.1;
		double mean = 11.942;
		String[] mode = new String[] { "6.6", "9.5" };
		double i75th = 15.9;
		double i97p5th = 22.04;
		double max = 23.0;

		assertEquals(statDate, folate02Jan2009.getIncludedDates().get(0));
		assertEquals(statType, folate02Jan2009.getAnalyteType());

		assertEquals(min, folate02Jan2009.getMin(), 0.001);
//		assertEquals(i2p5th, folate02Jan2009.getPercentile(0.025), 0.001);
//		assertEquals(i25th, folate02Jan2009.getPercentile(0.25), 0.001);
//		assertEquals(median, folate02Jan2009.getPercentile(0.5), 0.001);
		assertEquals(mean, folate02Jan2009.getMean(), 0.001);
		//assertTrue(Arrays.asList(mode).contains(folate02Jan2009.getMode()[0]));
//		assertEquals(i75th, folate02Jan2009.getPercentile(0.75), 0.001);
//		assertEquals(i97p5th, folate02Jan2009.getPercentile(0.975), 0.001);
		assertEquals(max, folate02Jan2009.getMax(), 0.001);

	}

	
	public void psaTest() {

		PSAdata p = new PSAdata();
	

		AnalyteStat psa = ComputeAnalyteStats.computeMonth(p.getMonth(7), "psa", 7);
		
		// Values from Excel

		int total = 2346;
		// numeric excluding <0.03 = 2277
		// numeric including 0.029 = 2339
		int totalValid = 2339;
		// other "U" = 7
		Integer countU = 7;

		// Min 0.029
		// 2.5 0.029
		// 25 0.71
		// 50 1.35
		// 75 3.16
		// 97.5 20.31
		// Max 10106

		// 65th 2.127

		// Mean: 9.458323215

		// TODO These test figures are wrong becuase we're now filtering out weekends!
		
//		assertEquals(total, psa.getInputCount());
//		assertEquals(totalValid, psa.getValidCount());
//		assertEquals(countU, psa.getOtherData().get("U"));
//
//		assertEquals(0.029, psa.getMin(), 0.001);
//
//		assertEquals(0.029, psa.getPercentile(0.025), 0.001);
//		assertEquals(0.71, psa.getPercentile(0.25), 0.001);
//		assertEquals(1.35, psa.getPercentile(0.5), 0.001);
//		assertEquals(3.16, psa.getPercentile(0.75), 0.001);
//		assertEquals(20.31, psa.getPercentile(0.975), 0.001);
//
//		assertEquals(2.127, psa.getPercentile(0.65), 0.001);
//
//		assertEquals(10106, psa.getMax(), 0.001);
//
//		assertEquals(9.458, psa.getMean(), 0.001);

		// TODO lots more tests
	}
}

/*
 * 
 * // Sample values for calculations // >24.0 // 2.8 // UXH
 */