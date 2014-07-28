package ie.brianhenry.veintobrain.representations;

import org.joda.time.LocalDate;

/**
 * A single test result
 * 
 * @author BrianHenry.ie
 *
 */
public class AnalyteResult {
	
	private String analyteType;
	private LocalDate date;
	private String result;
	
	public AnalyteResult(){}
	
	public AnalyteResult(String analyteType, LocalDate date, String result) {
		super();
		this.analyteType = analyteType;
		this.date = date;
		this.result = result;
	}
	
	public String getAnalyteType() {
		return analyteType;
	}
	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	

}
