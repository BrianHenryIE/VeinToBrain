package ie.brianhenry.veintobrain.core;

import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.primitives.Doubles;

// TODO Do we need to filter out ICQ samples??? 

public class ComputeAnalyteStats {

	/**
	 * The percentiles to be calculated for every AnalyteStat
	 */
	protected static final Double[] defaultPercentiles = { 0.025, 0.25, 0.5, 0.75, 0.975 };

	// TODO This should be analyte specific
	// See notes for dreams of calculating it
	public static final int MIN_TESTS = 12;

	public static boolean isValidDate(AnalyteDate date) {
		return isValidDate(new LocalDate(date.getDay()));
	}

	/**
	 * Checks the date is not a weekend, bank holiday
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isValidDate(LocalDate date) {

		return (date.getDayOfWeek() < 6 && !holidates.contains(date));
	}

	private static List<LocalDate> holidates = new ArrayList<LocalDate>();

	// static initializer. Runs once per class. Cousin of constructor
	static {
		// 2010-2015 // TODO Put in database, user configurable
		String[] holidays = { "01/01/2010", "17/03/2010", "05/04/2010", "03/05/2010", "07/06/2010", "02/08/2010", "25/10/2010",
				"25/12/2010", "26/12/2010", "27/12/2010", "28/12/2010", "03/01/2011", "17/03/2011", "25/04/2011", "02/05/2011",
				"06/06/2011", "01/08/2011", "31/10/2011", "25/12/2011", "26/12/2011", "27/12/2011", "01/01/2012", "02/01/2012",
				"02/01/2012", "17/03/2012", "19/03/2012", "19/03/2012", "09/04/2012", "09/04/2012", "07/05/2012", "07/05/2012",
				"04/06/2012", "04/06/2012", "06/08/2012", "06/08/2012", "29/10/2012", "29/10/2012", "30/11/2012", "25/12/2012",
				"25/12/2012", "26/12/2012", "26/12/2012", "01/01/2013", "17/03/2013", "18/03/2013", "01/04/2013", "06/05/2013",
				"03/06/2013", "05/08/2013", "28/10/2013", "25/12/2013", "26/12/2013", "01/01/2014", "17/03/2014", "21/04/2014",
				"05/05/2014", "02/06/2014", "04/08/2014", "27/10/2014", "25/12/2014", "26/12/2014", "01/01/2015", "17/03/2015",
				"06/04/2015", "04/05/2015", "01/06/2015", "03/08/2015", "26/10/2015", "25/12/2015", "26/12/2015", "28/12/2015" };
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		for (String h : holidays)
			holidates.add(dtf.parseLocalDate(h)); // inefficient but temporary
	}

	/**
	 * This does not check for weekends or bank holidays. Use isValidDate() for
	 * that.
	 * 
	 * @param day
	 * @param analyteType
	 * @return
	 */
	public static AnalyteStat computeDay(AnalyteDate day, String analyteType) {

		AnalyteStat as = new AnalyteStat(analyteType);
		as.setAnalytePeriod(StatPeriod.DAY);
		as.addDate(day.getDay());
		as.addIncludedDays(day);

		/**
		 * allReadings are unedited, includes U, UX, UXH, blanks, everything!
		 */
		as.setOriginalReadings(day.getResults());

		// Get the numeric readings
		as = parseReadings(as);

		// TODO Get the 4SD and remove

		if (as.getValidCount() <= MIN_TESTS || !isValidDate(day))
			as.setIsValid(false);
		else
			// Get the calculations
			compute(as);

		return as;
	}

	/**
	 * Checks each day is a valid day, has enough tests and is actually in this
	 * month, then combines the results and computes the stats
	 * 
	 * @param listOfDates
	 * @param analyteType
	 * @param month
	 * @return
	 */
	public static AnalyteStat computeMonth(List<AnalyteDate> listOfDates, String analyteType, int month) {
		AnalyteStat as = new AnalyteStat(analyteType);
		as.setAnalytePeriod(StatPeriod.MONTH);

		List<String> allReadings = new ArrayList<String>();

		for (AnalyteDate ad : listOfDates)
			if (computeDay(ad, analyteType).getIsValid() && new LocalDate(ad.getDay()).getMonthOfYear() == month) {
				allReadings.addAll(ad.getResults());
				as.addDate(ad.getDay());
			}

		as.setOriginalReadings(allReadings);
		as = parseReadings(as);

		if (as.getValidCount() <= MIN_TESTS)
			as.setIsValid(false);
		else
			compute(as);

		return as;
	}

	/**
	 * Takes a list of individual results, builds
	 * 
	 * @param analyteResults
	 * @param analyteType
	 * @return
	 */
	public static AnalyteStat computeOverall(List<AnalyteResult> analyteResults, String analyteType) {
		AnalyteStat as = new AnalyteStat(analyteType);
		as.setAnalytePeriod(StatPeriod.OVERALL);

		// HashMap for grouping results by date
		HashMap<LocalDate, List<String>> hm = new HashMap<LocalDate, List<String>>();

		for (AnalyteResult r : analyteResults) {
			if (hm.get(r.getDate()) == null)
				hm.put(r.getDate(), new ArrayList<String>());
			hm.get(r.getDate()).add(r.getResult());
		}

		List<AnalyteDate> listOfDates = new ArrayList<AnalyteDate>();

		for (LocalDate ld : hm.keySet())
			listOfDates.add(new AnalyteDate(analyteType, ld.toDate(), hm.get(ld)));

		List<String> allReadings = new ArrayList<String>();

		for (AnalyteDate ad : listOfDates)
			if (computeDay(ad, analyteType).getIsValid()) {
				allReadings.addAll(ad.getResults());
				as.addDate(ad.getDay());
			}

		// These are the readings only from valid days
		as.setOriginalReadings(allReadings);
		as = parseReadings(as);

		// TODO Get the 4SD and remove

		if (as.getValidCount() <= MIN_TESTS)
			as.setIsValid(false); // should be unreachable code!
		else
			compute(as);

		return as;

	}

	/**
	 * @param as
	 * @return A new AnalyteStat with only readings in it (no calculations)
	 */
	protected static AnalyteStat parseReadings(AnalyteStat as) {

		// why would there ever be zero here? //TODO maybe get rid of this
		if (as.getOriginalReadings().size() == 0)
			return as;

		AnalyteStat newAs = new AnalyteStat(as.getAnalyteType());
		newAs.setOriginalReadings(as.getOriginalReadings());
		newAs.setIncludedDates(as.getIncludedDates());

		/**
		 * allNumericReadings includes <0.3, >24 etc ±0.01
		 */
		List<Double> allNumericReadings = new ArrayList<Double>();

		HashMap<String, Integer> otherData = new HashMap<String, Integer>();

		/**
		 * This takes all the numbers from the readings and put them in the
		 * list. Any readings that aren't regular numbers are put in a HashMap
		 */
		for (String reading : as.getOriginalReadings())

			if (isPositiveNumeric(reading))
				allNumericReadings.add(Double.parseDouble(reading));
			else {
				/**
				 * Non-numeric readings are recoded in a HashMap periods are
				 * replaced with underscores for mongo
				 */
				String mongoReading;
				if (reading == null) // TODO this isn't working properly.
					mongoReading = "null_"; // TODO Is this a terrible solution?
				else
					mongoReading = reading.replace(".", "_");

				if (!otherData.containsKey(mongoReading))
					otherData.put(mongoReading, 0);

				otherData.put(mongoReading, otherData.get(mongoReading) + 1);

				/**
				 * each <0.03 etc is included in the calculations
				 */
				if (isPositiveNumeric(reading.replace(">", "")))
					allNumericReadings.add(Double.parseDouble(reading.replace(">", "")) + 0.001);
				else if (isPositiveNumeric(reading.replace("<", "")))
					allNumericReadings.add(Double.parseDouble(reading.replace("<", "")) - 0.001);
			}

		double s4d = standardDeviation(allNumericReadings) * 4;

		// Remove anything > 4SD
		Iterator<Double> d = allNumericReadings.iterator();
		Double next;
		while (d.hasNext()) {
			next = d.next();
			if (next > s4d) {
				if (!otherData.containsKey("4SD"))
					otherData.put("4SD", 0);
				otherData.put("4SD", otherData.get("4SD") + 1);
				d.remove();
			}
		}

		newAs.setNumericReadings(allNumericReadings);
		newAs.setOtherData(otherData);
		newAs.setInputCount(as.getOriginalReadings().size());
		newAs.setValidCount(allNumericReadings.size());

		// newAs.setReadingsDA(Doubles.toArray(allNumericReadings));

		return newAs;
	}

	protected static AnalyteStat compute(AnalyteStat as) {

		// Now we have the full list of readings ready to calculate the
		// percentiles etc.

		// for when we need it as an array
		double[] readings = Doubles.toArray(as.getNumericReadings());

		Arrays.sort(readings);

		as.setMin(round(readings[0], 3));
		as.setMax(round(readings[readings.length - 1], 3));

		for (double pth : defaultPercentiles)
			as.setPercentile(pth, percentile(readings, pth));

		as.setMean(round(getMean(readings), 3));

		// Calculate mode
		List<String> modes = new ArrayList<String>();
		HashMap<String, Integer> modeCount = new HashMap<String, Integer>();

		for (String reading : as.getOriginalReadings())
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

		as.setStandardDeviation(round(standardDeviation(as.getNumericReadings()), 3));

		return as;
	}

	private static double getMean(double[] readings) {
		double sum = 0;
		for (Double d : readings)
			sum += d;
		return (sum / readings.length);
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
	public static double percentile(double[] readings, double percentile) {

		if (percentile > 1)
			percentile = percentile / 100;

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
	private static boolean isPositiveNumeric(String str) {
		// match a number with possible decimal.
		return str == null ? false : str.matches("\\d+(\\.\\d+)?");
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

	public static void getMovingMeanOfMedian(List<AnalyteStat> analyteStats, int n) {

		HashMap<LocalDate, AnalyteStat> statsByDay = new HashMap<LocalDate, AnalyteStat>();

		// Prepare a hashmap of date:stat
		for (int i = 0; i < analyteStats.size(); i++)
			statsByDay.put(new LocalDate(analyteStats.get(i).getIncludedDates().get(0)), analyteStats.get(i));

		getMovingMeanOfMedian(statsByDay, n);

	}

	public static void getMovingMeanOfMedian(HashMap<LocalDate, AnalyteStat> statsByDay, int numberOfDays) {

		double sum;

		for (LocalDate d : statsByDay.keySet()) {
			sum = 0;
			sum += statsByDay.get(d).getPercentile(0.5);
			int included = 1;
			for (int i = 1; i < (3 * numberOfDays) && included < numberOfDays; i++) {
				if (statsByDay.get(d.minusDays(i)) != null && statsByDay.get(d.minusDays(i)).getIsValid()) {
					sum += statsByDay.get(d.minusDays(i)).getPercentile(0.5);
					included++;
					// System.out.println(d.toString() + " " + included + "/" +
					// numberOfDays);
					// + statsByDay.get(d.minusDays(i)).getPercentile(0.5) +
					// " : " + included);
				}
			}
			// System.out.println(included + " " + numberOfDays);
			if (included == numberOfDays) {
				statsByDay.get(d).addMovingMeanOfMedian(numberOfDays, (sum / numberOfDays));
				// System.out.println(d.toString() + "   " + included);
			}

			// TODO Maybe record if there weren't n previous days to work with
		}

	}

	public static void getMovingMean(HashMap<LocalDate, AnalyteStat> statsByDay, int numberOfDays) {

		double sum;
		int count = 0;

		for (LocalDate d : statsByDay.keySet()) {
			sum = 0;
			sum += statsByDay.get(d).getPercentile(0.5);
			int included = 1;
			for (int i = 1; i < (3 * numberOfDays) && included < numberOfDays; i++) {
				if (statsByDay.get(d.minusDays(i)) != null && statsByDay.get(d.minusDays(i)).getIsValid()) {
					for (double reading : statsByDay.get(d.minusDays(i)).getNumericReadings()) {
						sum += reading;
						count++;
					}
					included++;
				}
			}
			if (included == numberOfDays)
				statsByDay.get(d).addMovingMean(numberOfDays, (sum / count));

		}

	}

}