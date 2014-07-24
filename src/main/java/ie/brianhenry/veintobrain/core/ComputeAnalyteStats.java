package ie.brianhenry.veintobrain.core;

import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.primitives.Doubles;

public class ComputeAnalyteStats {

	/**
	 * The percentiles to be calculated for every AnalyteStat
	 */
	private static final Double[] defaultPercentiles = { 0.025, 0.25, 0.5, 0.75, 0.975 };

	public static final int MIN_TESTS = 10;

	public static AnalyteStat computeDay(AnalyteDate day, String analyteType) {

		AnalyteStat as = new AnalyteStat(analyteType);

		as.addDate(day.getDay());

		/**
		 * allReadings are unedited, includes U, UX, UXH, blanks, everything!
		 */
		List<String> allReadings = Arrays.asList(day.getResults());

		compute(as, allReadings);

		return as;
	}

	public static AnalyteStat computeMonth(List<AnalyteDate> listOfDates, String analyteType, int month) {
		AnalyteStat as = new AnalyteStat(analyteType);

		/**
		 * allReadings are unedited, includes U, UX, UXH, blanks, everything!
		 */
		List<String> allReadings = new ArrayList<String>();

		// TODO This is maybe unnecessary considering we'll be pulling from
		// the database by name
		// Check for weekdays (days 1-5)
		// Check for minimum (n) number of tests that day
		// TODO check for bank holidays
		for (AnalyteDate ad : listOfDates) {

			DateTime dt = new DateTime(ad.getDay());
			if (ad.getType().equals(analyteType) && dt.getMonthOfYear() == month && dt.getDayOfWeek() < 6
					&& ad.getResults().length > MIN_TESTS) {

				// We're going to use this one!
				as.addDate(ad.getDay());
				allReadings.addAll(Arrays.asList(ad.getResults()));

			}
		}

		if (allReadings.size() > 0)
			compute(as, allReadings);

		return as;
	}

	private static AnalyteStat compute(AnalyteStat as, List<String> allReadings) {

		/**
		 * allNumericReadings includes <0.3, >24 etc ±0.01
		 */
		List<Double> allNumericReadings = new ArrayList<Double>();

		/**
		 * This takes all the numbers from the readings and put them in the
		 * list. Any readings that aren't regular numbers are put in a HashMap
		 */
		for (String reading : allReadings)
			// TODO check for negative number
			if (isNumeric(reading))
				allNumericReadings.add(Double.parseDouble(reading));
			else {
				/**
				 * Non-numeric readings are recoded in a HashMap
				 */
				if (as.getOtherData().get(reading) != null)
					as.getOtherData().put(reading, as.getOtherData().get(reading) + 1);
				else
					as.getOtherData().put(reading, 1);

				/**
				 * each <0.03 etc is included in the calculations
				 */
				if (isNumeric(reading.replace(">", "")))
					allNumericReadings.add(Double.parseDouble(reading.replace(">", "")) + 0.001);
				else if (isNumeric(reading.replace("<", "")))
					allNumericReadings.add(Double.parseDouble(reading.replace("<", "")) - 0.001);
			}

		// Now we have the full list of readings ready to calculate the
		// percentiles etc.

		// for when we need it as an array
		double[] readings = Doubles.toArray(allNumericReadings);

		as.setInputCount(allReadings.size());
		as.setValidCount(readings.length);

		Arrays.sort(readings);

		as.setMin(round(readings[0], 3));
		as.setMax(readings[readings.length - 1]);

		for (double pth : defaultPercentiles)
			as.setPercentile(pth, percentile(readings, pth));

		double sum = 0;
		for (double d : readings)
			sum += d;
		as.setMean(round(sum / readings.length, 3));

		// Calculate mode
		List<String> modes = new ArrayList<String>();
		HashMap<String, Integer> modeCount = new HashMap<String, Integer>();

		for (String reading : allReadings)
			if (modeCount.get(reading) != null)
				modeCount.put(reading, modeCount.get(reading) + 1);
			else
				modeCount.put(reading, 1);

		int highestCount = 0;
		for (Integer i : modeCount.values())
			if (i > highestCount)
				highestCount = i;
		for (String s : modeCount.keySet())
			if (modeCount.get(s) == highestCount)
				modes.add(s);

		as.setMode(modes);

		return as;
	}

	/**
	 * Implements the same percentile algorithm as Excel
	 * 
	 * @param readings
	 * @param percentile
	 * @return
	 * @see http://www.utdallas.edu/~serfling/3332/ComputingPercentiles.pdf
	 * @see http://www.itl.nist.gov/div898/handbook/prc/section2/prc252.htm
	 */
	private static double percentile(double[] readings, double percentile) {

		// Repeatedly sorting it is inefficient but not as hard as sorting it
		// for the first time each time
		Arrays.sort(readings);

		double pIndex = 1 + percentile * (readings.length - 1) - 1;

		double calc = readings[(int) pIndex] + (pIndex % 1) * (readings[(int) (pIndex + 1)] - readings[(int) pIndex]);

		return round(calc, 3);
	}

	/**
	 * @param str
	 * @return
	 * @see http 
	 *      ://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is
	 *      -a-numeric-type-in-java
	 */
	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	/**
	 * Because doubles and floats aren't precise and DrT said 2 decimal places
	 * is adequate
	 * 
	 * @param value
	 * @param places
	 * @return
	 * @see http
	 *      ://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal
	 *      -places
	 * @see http://docs.oracle.com/cd/E19957-01/806-3568/ncg_goldberg.html
	 */
	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double standardDeviation(List<Double> readings) {

		double sum = 0;
		for (double r : readings)
			sum += r;

		double mean = sum / readings.size();

		double sumSqDif = 0;
		for (double r : readings)
			sumSqDif += Math.pow(r - mean, 2);

		return Math.sqrt(sumSqDif / readings.size());
	}

}
