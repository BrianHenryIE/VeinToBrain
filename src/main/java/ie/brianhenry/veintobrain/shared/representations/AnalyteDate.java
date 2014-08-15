package ie.brianhenry.veintobrain.shared.representations;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

/**
 * 
 * Shows single date results for a certain analyte
 * 
 * For a single day's raw data This isn't the way it comes from Cognos and maybe
 * isn't the best way to store it
 * 
 * @author BrianHenry.ie
 *
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public class AnalyteDate implements JsonSerializable {

	private String type;
	private Date day;
	private List<String> results;

	/**
	 * Constructor
	 */
	public AnalyteDate() {
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            type of analyte (calcium, phospate, potassium, ...)
	 * @param day
	 *            date of analysis
	 * @param results
	 *            results for that particular date (String type chosen in order
	 *            to manage non-numerical data
	 */
	public AnalyteDate(String type, Date day, List<String> results) {
		this.day = day;
		this.results = results;
		this.type = type;
	}

	/**
	 * @return type of analyte
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return date of analysis
	 */
	public Date getDay() {
		return this.day;
	}

	/**
	 * @return results for that particular date
	 */
	public List<String> getResults() {
		return results;

	}

	/**
	 * sets the type of the analyte
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * sets the date of analysis
	 * 
	 * @param day
	 */
	public void setDay(Date day) {
		this.day = day;
	}

	/**
	 * sets results for that particular date
	 * 
	 * @param results
	 */
	public void setResults(List<String> results) {
		this.results = results;
	}
}