package ie.brianhenry.veintobrain.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.Doubles;

/**
 * @author BrianHenry.ie
 *
 */
public class AnalyteStat {

	/**
	 * allNumericReadings includes <0.3, >24 etc ±0.01
	 */
	private List<Double> allNumericReadings = new ArrayList<Double>();

	/**
	 * allReadings are unedited, includes U, UX, UXH, blanks, everything!
	 */
	private List<String> allReadings = new ArrayList<String>();

	private final String analyteType;

	/**
	 * The percentiles to be calculated for every AnalyteStat
	 */
	private Double[] defaultPercentiles = { 0.025, 0.25, 0.5, 0.75, 0.975 };

	/**
	 * This object could be for one day, week, month, etc.
	 */
	private List<Date> includedDates = new ArrayList<Date>();

	private int inputCount;

	private HashMap<Double, Double> percentileCalculations = new HashMap<Double, Double>();

	private double max;
	private double min;
	private double mean;
	private String[] mode;

	private double[] readings; // for when we need it as an array

	private int validCount;

	/**
	 * Any non-numeric input data, e.g. U, UXINC, <0.3, >24
	 */
	private Map<String, Integer> otherData = new HashMap<String, Integer>();

	public AnalyteStat(AnalyteDate day) {
		includedDates.add(day.getDay());
		analyteType = day.getType();
		inputCount = day.getResults().length;
		processNumbersFromReadings(day.getResults());
		processNumbersFromOtherData();
		calculateStats();
	}

	public AnalyteStat(String analyteType, List<AnalyteDate> days) {

		this.analyteType = analyteType;

		// TODO somewhere check that they're not weekends etc
		// TODO check for negative number

		analyteType = days.get(0).getType();

		for (AnalyteDate day : days) {
			inputCount += day.getResults().length;
			processNumbersFromReadings(day.getResults());
			includedDates.add(day.getDay());

			// Check that they're all the same type of analyte
			if (!analyteType.equals(day.getType()))
				return; // TODO deal with this properly
		}
		processNumbersFromOtherData();
		calculateStats();
	}

	/**
	 * This takes all the numbers from the readings and put them in the list.
	 * Any readings that aren't regular numbers are put in a HashMap
	 * 
	 * @param results
	 */
	private void processNumbersFromReadings(String[] results) {

		allReadings.addAll(Arrays.asList(results));

		for (String reading : results)
			if (AnalyteStat.isNumeric(reading))
				allNumericReadings.add(Double.parseDouble(reading));
			else if (otherData.get(reading) != null)
				otherData.put(reading, otherData.get(reading) + 1);
			else
				otherData.put(reading, 1);
	}

	/**
	 * The HashMap of non-numeric readings is processed to include each <0.03
	 * etc in the calculations
	 */
	private void processNumbersFromOtherData() {

		// include >24 and <0.3
		for (String other : otherData.keySet())
			if (isNumeric(other.replace(">", "")))
				allNumericReadings.add(Double.parseDouble(other
						.replace(">", "")) + 0.001);
			else if (isNumeric(other.replace("<", "")))
				for (int i = 0; i < otherData.get(other); i++)
					allNumericReadings.add(Double.parseDouble(other.replace(
							"<", "")) - 0.001);

	}

	private void calculateStats() {

		readings = Doubles.toArray(allNumericReadings);

		inputCount = allReadings.size();
		validCount = readings.length;

		Arrays.sort(readings);

		min = readings[0];

		for (double p : defaultPercentiles)
			percentileCalculations.put(p, percentile(readings, p));

		max = readings[readings.length - 1];

		double sum = 0;
		for (double d : readings)
			sum += d;
		mean = sum / readings.length;

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

		mode = modes.toArray(new String[modes.size()]);

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
	private double percentile(double[] readings, double percentile) {

		double pIndex = 1 + percentile * (readings.length - 1) - 1;

		double calc = readings[(int) pIndex] + (pIndex % 1)
				* (readings[(int) (pIndex + 1)] - readings[(int) pIndex]);

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

	public double getMin() {
		return round(min, 3);
	}

	public HashMap<Double, Double> getPercentiles() {
		return percentileCalculations;
	}

	/**
	 * TODO This should maybe store in the database whenever it's called
	 * 
	 * @param p
	 * @return
	 */
	public double getPercentile(double p) {
		if (!percentileCalculations.containsKey(p))
			percentileCalculations.put(p, percentile(readings, p));

		return round(percentileCalculations.get(p), 3);
	}

	public double getMean() {
		return round(mean, 3);
	}

	public String[] getMode() {
		return mode;
	}

	public double getMax() {
		return round(max, 3);
	}

	public int getValidCount() {
		return validCount;
	}

	public int getInputCount() {
		return inputCount;
	}

	public List<Date> getIncludedDates() {
		return includedDates;
	}

	public String getType() {
		return analyteType;
	}

	public Map<String, Integer> getOtherData() {
		return otherData;
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

}
