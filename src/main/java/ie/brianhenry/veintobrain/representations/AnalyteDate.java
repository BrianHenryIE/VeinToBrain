package ie.brianhenry.veintobrain.representations;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author BrianHenry.ie
 * 
 *         For a single day's raw data This isn't the way it comes from Cognos
 *         and probably isn't the best way to store it
 *
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public class AnalyteDate {

	private String type;
	private Date day;
	private String[] results;

	public AnalyteDate() {
	}

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

	public void setType(String type) {
		this.type = type;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public void setResults(String[] results) {
		this.results = results;
	}
}