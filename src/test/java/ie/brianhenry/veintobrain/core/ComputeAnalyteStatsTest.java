package ie.brianhenry.veintobrain.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComputeAnalyteStatsTest {

	PSAdata pd = new PSAdata();
	String[] readingsS = { "11.1", "14.5", "17.9", "8.7", "15.7", "12.3", "2.8", "14.9", "4.5", "7.8", "6.6", "10.6", "23.0", "19.0",
			"4.7", "9.5", "7.1", "13.5", "9.5", "3.5", "16.1", "21.7", "11.2", "9.8", "13.8", "20.7", "6.6", "4.2", "6.2", "19.9",
			"15.9", "21.8", "9.0" };
	double[] readingsD = { 11.1, 14.5, 17.9, 8.7, 15.7, 12.3, 2.8, 14.9, 4.5, 7.8, 6.6, 10.6, 23.0, 19.0, 4.7, 9.5, 7.1, 13.5, 9.5,
			3.5, 16.1, 21.7, 11.2, 9.8, 13.8, 20.7, 6.6, 4.2, 6.2, 19.9, 15.9, 21.8, 9.0 };

	private List<AnalyteStat> allDailyAnalyteStats = new ArrayList<AnalyteStat>();

	List<AnalyteResult> analyteResultsList;
	List<AnalyteDate> allVaildAnalyteDates = new ArrayList<AnalyteDate>();
	List<AnalyteDate> allInVaildAnalyteDates = new ArrayList<AnalyteDate>();
	
	@Before
	public void setup() {
		// MIN_TESTS = 12

		analyteResultsList = pd.getResults();

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
				allDailyAnalyteStats.add(s);
				allVaildAnalyteDates.add(d);
			} else
				allInVaildAnalyteDates.add(d);
		}


	}

	// @Test
	public void TestOverall() {

		AnalyteStat overall = ComputeAnalyteStats.computeOverall(analyteResultsList, "psa");

		List<Double> allMedians = new ArrayList<Double>();

		double sumOfMedian = 0.0;
		int countValidStats = 0;
		for (AnalyteStat stat : allDailyAnalyteStats)
			if (stat.getIsValid()) {
				sumOfMedian += stat.getPercentile(0.5);
				allMedians.add(stat.getPercentile(0.5));
				countValidStats++;
			}

		double meanOfMedians = sumOfMedian / countValidStats;

		System.out.println("meanOfMedians : " + meanOfMedians);

		double sdMedians = ComputeAnalyteStats.standardDeviation(allMedians);

		System.out.println("sd of the medians : " + sdMedians);

		double medianCV = sdMedians / meanOfMedians;

		System.out.println("medianCV : " + medianCV);

		// Get an addressable list of the daily stats so we can ask for the
		// previous days' stats
		// for the moving mean

		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(overall));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// @Test
	public void computeDayTest() {

		// pd.getMonth(1).get(1).getResults()

		AnalyteDate ad = new AnalyteDate("psa", new LocalDate("2014-08-06").toDate(), pd.getMonth(1).get(1).getResults());

		AnalyteStat as = ComputeAnalyteStats.computeDay(ad, "psa");

		// Excel
		double mean = 2.293;
		double stdev = 3.09;

		double p0025 = 0.029;
		double p025 = 0.53;
		double median = 1.17;
		double p075 = 2.34;
		double p975 = 11.557;
		double min = 0.03;
		double max = 19.80;

		assertEquals(p0025, as.getPercentile(0.025), 0.001);
		assertEquals(p025, as.getPercentile(0.25), 0.001);
		assertEquals(median, as.getPercentile(50), 0.001);
		assertEquals(p075, as.getPercentile(0.75), 0.001);
		assertEquals(p975, as.getPercentile(0.975), 0.001);

		assertEquals(mean, as.getMean(), 0.001);
		assertEquals(min, as.getMin(), 0.001);
		assertEquals(max, as.getMax(), 0.001);
		assertEquals(stdev, as.getStandardDeviation(), 0.01);
	}

	// @Test
	public void getMovingMeanTest() {

		// So we give in a list of AnalyteStats and a number of days for the
		// calculation
		// We store the calculation with the most recent stat
		// if we pass a list of 50 and want the 20 day mm the first 19 are null
		// for that

		// hmmm... I think there's no need to return the object here. it's being
		// operated on when it's in there
		// statsByDay = ComputeAnalyteStats.getMovingMeanOfMedian(statsByDay, 7);

	}

	// @Test
	public void parseReadingsTest() {

		// pd.getMonth(1).get(1).getResults()
		// [0.6, 0.81, 0.46, 0.46, 0.82, 1, 0.22, 0.79, 0.05, 0.54, 0.25, 1.08,
		// 2.33, 1.17, 2.87, 5.51, 0.56, 0.8, 1.86, 0.79, 1.59, 0.29, 0.5, 1.78,
		// 0.04, 0.35, <0.03, 1.58, 12.26, 6.94, 0.43, 0.2, 1.65, 1.25, 0.68,
		// 6.43, 0.24, 10.25, 0.88, 0.72, 0.72, 6.79, 2.54, 0.55, 8.32, 6.37,
		// 5.6, 0.53, 1.8, 0.59, 2.11, 0.36, 2.35, 0.95, 1.17, 1.78, 1.63, 0.85,
		// 0.69, 3.03, 1.43, 0.45, 0.19, 2.76, 0.64, 0.8, 0.78, 2.17, 13.06,
		// <0.03, 8.2, 5.18, 2.01, 0.43, 1.96, 1.27, 4.04, 5.45, 7.22, <0.03,
		// 2.08, 19.8, 0.74, 1.68, 2.04, 4.89, 0.33, 0.25, 1.45, 0.52, 14.6,
		// 6.51, 1.13, 2.31, 0.53, 3.15, 1.29, <0.03, 1.82, 0.51, 1.13, 3.74,
		// 1.25, 1.03, 1.38, 1.4, 1.66, 3.64, 6, 0.47, 0.42, 6.11, 3.7, <0.03,
		// 0.48, 0.69, 2.49, 0.53, 2.96, 0.85, 3.87, 0.5, 1.62, 0.68, 0.13,
		// 1.34, 9.22, 0.47, 2.07, 1.68, 1.12, 1.12, <0.03, 0.06, 2.19]

		// Excel says:
		int totalReadings = 135;
		int otherDataTypes = 1;
		Integer otherData = 6;

		AnalyteStat small = new AnalyteStat("psa");
		small.setOriginalReadings(pd.getMonth(1).get(1).getResults());

		small = ComputeAnalyteStats.parseReadings(small);

		assertEquals(totalReadings, small.getValidCount());
		assertEquals(otherDataTypes, small.getOtherData().size());
		assertEquals(otherData, small.getOtherData().entrySet().iterator().next().getValue());

	}

	// TODO Test isValidDate(AnalyteDate)

	// @Test
	public void isValidDateTest() {

		// Christmas is invalid
		// the last Thursday in July is probably valid
		// any sunday is invalid
		// any saturday in invalid
		LocalDate christmas = new LocalDate("2014-12-25");
		LocalDate lastThursdayInJuly = new LocalDate("2014-07-31");
		LocalDate aSunday = new LocalDate("2014-03-16");
		LocalDate aSaturday = new LocalDate("2014-04-05");

		assertTrue(!ComputeAnalyteStats.isValidDate(christmas));
		assertTrue(ComputeAnalyteStats.isValidDate(lastThursdayInJuly));
		assertTrue(!ComputeAnalyteStats.isValidDate(aSunday));
		assertTrue(!ComputeAnalyteStats.isValidDate(aSaturday));

	}

	// @Test
	public void percentileTest() {

		// Excel calculations:
		double i2p5th = 3.36;
		double i25th = 7.1;
		double median = 11.1;
		double i75th = 15.9;
		double i97p5th = 22.04;

		assertEquals(i2p5th, ComputeAnalyteStats.percentile(readingsD, 0.025), 0.01);
		assertEquals(i25th, ComputeAnalyteStats.percentile(readingsD, 25), 0.01);
		assertEquals(median, ComputeAnalyteStats.percentile(readingsD, 0.5), 0.01);
		assertEquals(i75th, ComputeAnalyteStats.percentile(readingsD, 0.75), 0.01);
		assertEquals(i97p5th, ComputeAnalyteStats.percentile(readingsD, 97.5), 0.01);
	}

	// @Test
	public void standardDeviationTest() {

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
