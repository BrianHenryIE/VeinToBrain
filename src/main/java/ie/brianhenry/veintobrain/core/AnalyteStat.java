package ie.brianhenry.veintobrain.core;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.math3.stat.StatUtils;

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

	public AnalyteStat(Date statDate, String statType, double[] readings) {

		Arrays.sort(readings);
		
		this.statDate = statDate;
		this.statType = statType;

		min = readings[0];

		Double[] defaultPercentiles = {0.025, 0.25, 0.5, 0.025, 0.75, 0.975};
		
		for(double p : defaultPercentiles)
			percentiles.put(p, percentile(readings, p));
		
		max = readings[readings.length-1];
		
		// TODO How many decimal places?
		mean = ((int) (StatUtils.mean(readings)*10))/10.0;
		mode = StatUtils.mode(readings);
	}

	// http://www.utdallas.edu/~serfling/3332/ComputingPercentiles.pdf
	// http://www.itl.nist.gov/div898/handbook/prc/section2/prc252.htm
	private double percentile(double[] readings, double percentile) {
		
		double pIndex = 1 + percentile * (readings.length - 1) - 1;
		
		return readings[(int) pIndex]
				+ (pIndex % 1)
				* (readings[(int) (pIndex + 1)] - readings[(int) pIndex]);

	}

	public double getMin() {
		return min;
	}

	public HashMap<Double, Double> getPercentiles() {
		return percentiles;
	}
	
	public double getPercentile(double p){
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

}
