package ie.brianhenry.veintobrain.representations;

import java.util.HashMap;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

/**
 * Config for a particular analyte beyond the dafaults used when computing the
 * stats
 * 
 * Should this be per user?
 * 
 * @author BrianHenry.ie
 *
 */
public class AnalyteConfig implements JsonSerializable {

	private String analyteType;

	private double overallMean;
	
	private double overallSD;
	
	private HashMap<String, Double> maxMovingMean = new HashMap<String, Double>();
	
	


	public String getAnalyteType() {
		return analyteType;
	}

	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}



}
