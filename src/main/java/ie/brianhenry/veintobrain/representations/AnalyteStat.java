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
 * @author BrianHenry.ie
 *
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public class AnalyteStat implements JsonSerializable {

	private String analyteType;

	/**
	 * This object could be for one day, week, month, etc.
	 */
	private List<Date> includedDates = new ArrayList<Date>();

	private int inputCount;

	private HashMap<String, Double> percentileCalculations = new HashMap<String, Double>();

	private double max;
	private double min;
	private double mean;
	private List<String> mode;

	/**
	 * Any non-numeric input data, e.g. U, UXINC, <0.3, >24
	 */
	private HashMap<String, Integer> otherData = new HashMap<String, Integer>();

	private int validCount;

	public AnalyteStat() {
	}

	public AnalyteStat(String analyteType) {
		this.analyteType = analyteType;
	}

	public HashMap<String, Double> getPercentiles() {
		return percentileCalculations;
	}

	public String getAnalyteType() {
		return analyteType;
	}

	public int getInputCount() {
		return inputCount;
	}

	public List<Date> getIncludedDates() {
		return includedDates;
	}

	public double getMean() {
		return mean;
	}

	public double getMin() {
		return min;
	}

	public List<String> getMode() {
		return mode;
	}

	public double getMax() {
		return max;
	}

	public HashMap<String, Integer> getOtherData() {
		return otherData;
	}

	public HashMap<String, Double> getPercentileCalculations() {
		return percentileCalculations;
	}

	// TODO What to do when a percentile hasn't been precalculated? RPC magic
	public double getPercentile(double p) {
		// if (!percentileCalculations.containsKey(p))
		// percentileCalculations.put(p, percentile(readings, p));

		return percentileCalculations.get(Double.toString(p));
	}

	public String getType() {
		return analyteType;
	}

	public int getValidCount() {
		return validCount;
	}

	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}

	public void setIncludedDates(List<Date> includedDates) {
		this.includedDates = includedDates;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void setMode(List<String> mode) {
		this.mode = mode;
	}

	public void setOtherData(HashMap<String, Integer> otherData) {
		this.otherData = otherData;
	}

	public void setPercentileCalculations(HashMap<String, Double> percentileCalculations) {
		this.percentileCalculations = percentileCalculations;
	}

	public void setPercentile(double percentile, double value) {
		percentileCalculations.put(Double.toString(percentile), value);
	}

	public void setValidCount(int validCount) {
		this.validCount = validCount;
	}

	public void addDate(Date day) {
		includedDates.add(day);
	}

}
