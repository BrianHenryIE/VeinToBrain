package ie.brianhenry.veintobrain.representations;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

/**
 * Config for a particular analyte beyond the dafaults used when computing the stats
 * 
 * Should this be per user?
 * 
 * @author BrianHenry.ie
 *
 */
public class AnalyteConfig implements JsonSerializable {
	
	private String username;
	
	private String analyteType;
	
	private Double[] additionalPercentiles;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAnalyteType() {
		return analyteType;
	}

	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}

	public Double[] getAdditionalPercentiles() {
		return additionalPercentiles;
	}

	public void setAdditionalPercentiles(Double[] additionalPercentiles) {
		this.additionalPercentiles = additionalPercentiles;
	}
	

	
	
}
