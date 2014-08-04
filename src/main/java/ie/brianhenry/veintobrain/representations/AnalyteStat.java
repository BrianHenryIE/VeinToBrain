package ie.brianhenry.veintobrain.representations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

/**
 * Implements all the statistics associated to each analyte and the set/get
 * methods
 */
/**
 * @author BrianHenry.ie
 *
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public class AnalyteStat implements JsonSerializable {

	@net.vz.mongodb.jackson.Id
	public String id; // UUID.randomUUID().toString();

	/**
	 * The type of analyte (calcium, phospate, potassium, ...)
	 */
	private String analyteType;

	// Transient stuff useful for calculations... will never be passed to client
	// or saved and are just object references while they exist

	private List<AnalyteDate> includedDays = new ArrayList<AnalyteDate>();

	private List<String> originalReadings = new ArrayList<String>();

	private List<Double> numericReadings = new ArrayList<Double>();

//	private double[] readingsDA;

	private double standardDeviation;

	/**
	 * Daily, weekly, monthly,yearly,total
	 */
	private StatPeriod analytePeriod;

	/**
	 * A stat would be invalid if it was weekend or too few readings
	 */
	private boolean isValid = true; // TODO should we be optimistic here?

	/**
	 * This object could be for one day, week, month, etc.
	 */
	private List<Date> includedDates = new ArrayList<Date>();

	/**
	 * Counts all the data read
	 */
	private int inputCount;

	private double max;

	/**
	 * Associates the percentiles values stored as strings ("0.25", "0.5", ...)
	 * to the proper value for that percentile. This is done in order to avoid
	 * frequent recalculations that can slow down the process.
	 */
	private HashMap<String, Double> percentileCalculations = new HashMap<String, Double>();

	private double min;

	/**
	 * Counts the numeric input data
	 */
	private int validCount;

	/**
	 * It is constructed as a list of strings in case of multiple modes.
	 */
	private List<String> mode;

	/**
	 * Associates any non-numeric input data (e.g. U, UXINC, <0.3, >24) with its
	 * number of occurrences.
	 */
	private HashMap<String, Integer> otherData = new HashMap<String, Integer>();

	private double mean;

	/**
	 * The moving mean of the 50th percentile <String, Double>=<number of
	 * days/weeks included, value>
	 */	
	private HashMap<String, Double> movingMeanOfMedians = new HashMap<String, Double>();

	private HashMap<String, Double> movingMean = new HashMap<String, Double>();

	public enum StatPeriod {
		DAY, WEEK, MONTH, YEAR, OVERALL
	}

	/**
	 * Constructor
	 */
	public AnalyteStat() {
	}

	/**
	 * Constructor
	 * 
	 * @param analyteType
	 */
	public AnalyteStat(String analyteType) {
		this.analyteType = analyteType;
	}


	public List<AnalyteDate> getIncludedDays() {
		return includedDays;
	}


	public void addIncludedDays(AnalyteDate date) {
		includedDays.add(date);
	}


	public List<String> getOriginalReadings() {
		return originalReadings;
	}


	public void setOriginalReadings(List<String> readings) {
		this.originalReadings = readings;
	}


	public List<Double> getNumericReadings() {
		return numericReadings;
	}


	public void setNumericReadings(List<Double> readings) {
		this.numericReadings = readings;
		this.validCount = readings.size();
	}

//	
//	public double[] getReadingsDA() {
//		return readingsDA;
//	}

//	
//	public void setReadingsDA(double[] readingsDA) {
//		this.readingsDA = readingsDA;
//	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public StatPeriod getAnalytePeriod() {
		return analytePeriod;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}


	public void setIncludedDays(List<AnalyteDate> includedDays) {
		this.includedDays = includedDays;
	}

	public void setAnalytePeriod(StatPeriod analytePeriod) {
		this.analytePeriod = analytePeriod;
	}

	public HashMap<String, Double> getMovingMeanOfMedians() {
		return movingMeanOfMedians;
}
	public HashMap<String, Double> getMovingMean() {
		return movingMean;
	}

	public void setMovingMean(HashMap<String, Double> movingMean) {
		this.movingMean = movingMean;
	}


	public void setMovingMeanOfMedians(HashMap<String, Double> movingMean) {
		this.movingMeanOfMedians = movingMean;
	}

	public void setMovingMean(String length, Double value) {
		movingMeanOfMedians.put(length, value);
	}

	public double getCoefficientOfVaritation(){
		return standardDeviation/mean;
	}

	/**
	 * @return the type of the analyte
	 */
	public String getAnalyteType() {
		return analyteType;
	}

	/**
	 * @return the number of values of the analyte
	 */
	public int getInputCount() {
		return inputCount;
	}

	/**
	 * @return the list of dates of the analyte
	 */
	public List<Date> getIncludedDates() {
		return includedDates;
	}

	/**
	 * @return the hashmap containing the values for each percentile (0.025,
	 *         0.25, 0.5, 0.75, 0.975)
	 */
	public HashMap<String, Double> getPercentileCalculations() {
		return percentileCalculations;
	}

	// TODO What to do when a percentile hasn't been precalculated? RPC magic
	/**
	 * @param p
	 *            the percentile to show
	 * @return the values for the particular percentile selected through the
	 *         parameter p
	 */
	public double getPercentile(double p) {
		// if (!percentileCalculations.containsKey(p))
		// percentileCalculations.put(p, percentile(readings, p));
		if (p > 1)
			p = p / 100;
		return percentileCalculations.get(Double.toString(p).replace(".", "_"));
	}

	/**
	 * @return the maximum value of the analyte
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @return the minimum value of the analyte
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @return the arithmetic mean
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * @return the mode(or modes) of the analyte
	 */
	public List<String> getMode() {
		return mode;
	}

	/**
	 * @return the hashmap containing the different non-numeric values plus
	 *         their number of occurrences
	 */
	public HashMap<String, Integer> getOtherData() {
		return otherData;
	}

	/**
	 * @return the number of numeric values of the analyte
	 */
	public int getValidCount() {
		return validCount;
	}

	/**
	 * sets the type of analyte
	 * 
	 * @param analyteType
	 */
	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}

	/**
	 * sets the dates of the analyte
	 * 
	 * @param includedDates
	 */
	public void setIncludedDates(List<Date> includedDates) {
		this.includedDates = includedDates;
	}

	/**
	 * sets the total amount of the analyte
	 * 
	 * @param inputCount
	 */
	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	/**
	 * sets the maximum value of the analyte
	 * 
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * sets the minimum value of the analyte
	 * 
	 * @param min
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * sets the arithmetic mean of the analyte
	 * 
	 * @param mean
	 */
	public void setMean(double mean) {
		this.mean = mean;
	}

	/**
	 * sets the mode (or modes) of the analyte
	 * 
	 * @param mode
	 */
	public void setMode(List<String> mode) {
		this.mode = mode;
	}

	/**
	 * sets the non-numeric values of the analyte
	 * 
	 * @param otherData
	 */
	public void setOtherData(HashMap<String, Integer> otherData) {
		this.otherData = otherData;
	}

	/**
	 * sets the values for each percentile of the analyte
	 * 
	 * @param percentileCalculations
	 */
	public void setPercentileCalculations(HashMap<String, Double> percentileCalculations) {
		this.percentileCalculations = percentileCalculations;
	}

	/**
	 * sets the percentile value of the analyte
	 * 
	 * @param percentile
	 * @param value
	 *            value of the percentile
	 */
	public void setPercentile(double percentile, double value) {
		percentileCalculations.put(Double.toString(percentile).replace(".", "_"), value);
	}

	/**
	 * sets the numeric values of the analyte
	 * 
	 * @param validCount
	 */
	public void setValidCount(int validCount) {
		this.validCount = validCount;
	}

	/**
	 * sets the dates of the analyte
	 * 
	 * @param day
	 */
	public void addDate(Date day) {
		includedDates.add(day);
	}

	public void addMovingMeanOfMedian(int numDays, double value) {
		movingMeanOfMedians.put(Integer.toString(numDays), value);
	}


	public void addMovingMean(int numDays, double value) {
		movingMean.put(Integer.toString(numDays), value);
	}

}
