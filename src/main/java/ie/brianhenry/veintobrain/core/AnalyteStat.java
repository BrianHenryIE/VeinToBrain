package ie.brianhenry.veintobrain.core;

import java.util.Arrays;
import java.util.Date;

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

	private double min;
	private double i2p5th;
	private double i25th;
	private double median;
	private double mean;
	private double[] mode;
	private double i75th;
	private double i97p5th;
	private double max;

	public AnalyteStat(Date statDate, String statType, double[] readings) {

		Arrays.sort(readings);
		
		this.statDate = statDate;
		this.statType = statType;

		min = readings[0];

		i2p5th = percentile(readings, 0.025);
		i25th = percentile(readings, 0.25);
		
		median = percentile(readings, 0.5);
		i75th = percentile(readings, 0.75);
		
		i97p5th = percentile(readings, 0.975);
		
		max = readings[readings.length-1];
		
		// TODO How many decimal places?
		mean = ((int) (StatUtils.mean(readings)*10))/10.0;
		mode = StatUtils.mode(readings);
	}

	// http://www.utdallas.edu/~serfling/3332/ComputingPercentiles.pdf
	// http://www.itl.nist.gov/div898/handbook/prc/section2/prc252.htm
	private double percentile(double[] readings, double percentile) {
		
		double percentileIndex = 1 + percentile * (readings.length - 1);
		
		System.out.println(percentile + " : " + percentileIndex);
		
		double javaIndex = percentileIndex - 1;
		
		System.out.println(percentile + " : readings["+ ((int) javaIndex) + "] " + readings[(int) javaIndex]);

		return readings[(int) javaIndex]
				+ (javaIndex % 1)
				* (readings[(int) (javaIndex + 1)] - readings[(int) javaIndex]);

	}

	public double getMin() {
		return min;
	}

	public double get2p5th() {
		return i2p5th;
	}

	public double get25th() {
		return i25th;
	}

	public double getMedian() {
		return median;
	}

	public double getMean() {
		return mean;
	}

	public double[] getMode() {
		return mode;
	}

	public double get75th() {
		return i75th;
	}

	public double get97p5th() {
		return i97p5th;
	}

	public double getMax() {
		return max;
	}

}
