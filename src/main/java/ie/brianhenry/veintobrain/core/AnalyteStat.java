package ie.brianhenry.veintobrain.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.StatUtils;

import com.google.common.primitives.Doubles;



public class AnalyteStat {

	final private Date statDate;
	final private String statType;

	public Date getDate() {
		return statDate;
	}

	public String getType() {
		return statType;
	}

	private HashMap<Double, Double> percentiles = new HashMap<Double, Double>();

	private double min;
	private double mean;
	private double[] mode;
	private double max;

	List<Double> allNumericReadings = new ArrayList<Double>();
	Map<String, Integer> otherData = new HashMap<String, Integer>();

	double[] readings; // for when we need it as an array

	public AnalyteStat(List<AnalyteDate> days) {

		// TODO Check that everything in the list is of the same type of analyte

		for (AnalyteDate day : days)
			processReadingsFromStrings(day.getResults());

		this.statDate = days.get(days.size() - 1).getDay();
		this.statType = days.get(0).getType();

		calculate();
	}

	public AnalyteStat(AnalyteDate day) {
		statDate = day.getDay();
		statType = day.getType();
		processReadingsFromStrings(day.getResults());
		calculate();
	}

	private void processReadingsFromStrings(String[] results) {
		for (String reading : results) {
			if (AnalyteStat.isNumeric(reading))
				allNumericReadings.add(Double.parseDouble(reading));
			else if (otherData.get(reading) != null)
				otherData.put(reading, otherData.get(reading) + 1);
			else
				otherData.put(reading, 1);
		}
	}

	private void calculate() {

		// readings = (Double[]) allNumericReadings.toArray();

		readings = Doubles.toArray(allNumericReadings);

		Arrays.sort(readings);

		min = readings[0];

		Double[] defaultPercentiles = { 0.025, 0.25, 0.5, 0.025, 0.75, 0.975 };

		for (double p : defaultPercentiles)
			percentiles.put(p, percentile(readings, p));

		max = readings[readings.length - 1];

		// TODO How many decimal places?
		mean = ((int) (StatUtils.mean(readings) * 10)) / 10.0;
		mode = StatUtils.mode(readings);
	}

	// http://www.utdallas.edu/~serfling/3332/ComputingPercentiles.pdf
	// http://www.itl.nist.gov/div898/handbook/prc/section2/prc252.htm
	private double percentile(double[] readings2, double percentile) {

		double pIndex = 1 + percentile * (readings2.length - 1) - 1;

		return readings2[(int) pIndex] + (pIndex % 1)
				* (readings2[(int) (pIndex + 1)] - readings2[(int) pIndex]);

	}

	// http://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-a-numeric-type-in-java
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public double getMin() {
		return min;
	}

	public HashMap<Double, Double> getPercentiles() {
		return percentiles;
	}

	public double getPercentile(double p) {
		return percentiles.get(p); // TODO what about null
	}

	public double getMean() {
		return mean;
	}

	public double[] getMode() {
		return mode;
	}

	public double getMax() {
		return max;
	}

	// TODO Temp until we figure something seneible
	private String title;

	public void setTitle(String name) {
		this.title = name;
	}

	public String getTitle() {
		return title;
	}

}
