package ie.brianhenry.veintobrain.core;

import java.util.Date;

/**
 * @author BrianHenry.ie
 * 
 *         For a single day's raw data
 *
 */
public class AnalyteDate {

	String type;
	Date day;
	String[] results;

	public AnalyteDate(String type, Date day, String[] results) {
		this.day = day;
		this.results = results;
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public Date getDay() {
		return this.day;
	}

	public String[] getResults() {
		return results;

	}
}